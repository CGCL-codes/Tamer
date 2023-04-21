package alesis.fusion.objects;

import alesis.fusion.Constant;
import alesis.fusion.Tables;
import alesis.fusion.Utility;
import alesis.fusion.commonparameters.*;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jam
 * @todo    Creating a new FusionCommonObject implies creating a number of
 *          CommonParameter objects as well. If we first create the object and
 *          then CPs are loaded from disk, a huge amount of resources are wasted.
 *          Because of this, a new constructor to specify if the object must be
 *          initiated or not must be written. It applies to EQs, ARPs, etc.
 */
public abstract class FusionCommonObject extends FusionObject {

    /** Array containing all the Common Parameters */
    protected Vector<CommonParameter> commonParameters;

    /**
     *
     */
    protected Vector<CommonParameter> fxCommonParameters;

    /**
     *
     */
    protected Vector<CommonParameter> fxICommonParameters;

    /**
     *
     */
    protected Vector<CommonParameter> fxBCommonParameters;

    /**
     *
     */
    protected Vector<CommonParameter> fxEqCommonParameters;

    /**
     *
     */
    protected Vector<CommonParameter> trackCommonParameters;

    /**
     *
     */
    public Arpeggiator[] arps;

    /**
     *
     */
    public FxCommonBlock.EqBlock EQ;

    /**
     *
     */
    public InsertFx[] insertFxs;

    public BusFx[] busFxs;

    /**
     *
     */
    public Switches switches;

    public int addCommonParameter(CommonParameter parameter) {
        commonParameters.add(parameter);
        return commonParameters.size();
    }

    /**
     *
     * @return
     */
    public int getGommonParametersNumber() {
        return commonParameters.size();
    }

    /**
     *
     * @param i
     * @return
     */
    public CommonParameter getCommonParameter(int i) {
        return (CommonParameter) commonParameters.elementAt(i);
    }

    /**
     *
     * @param i
     */
    public void removeCommonParameter(int i) {
        if (commonParameters.size() > i) {
            commonParameters.remove(i);
        }
    }

    FusionCommonObject(int numberOfArps) {
        numberOfArps = Utility.adjustRange(numberOfArps, 1, 4);
        arps = new Arpeggiator[numberOfArps];
        for (int i = 0; i < numberOfArps; i++) {
            arps[i] = new Arpeggiator(i);
        }
        EQ = new FxCommonBlock.EqBlock();
        insertFxs = new InsertFx[Constant.INSERT_FX_NUMBER];
        busFxs = new BusFx[Constant.BUS_FX_NUMBER];
        switches = new Switches();
        commonParameters = new Vector();
        fxCommonParameters = new Vector();
        fxICommonParameters = new Vector();
        fxICommonParameters.ensureCapacity(4 * 14);
        fxBCommonParameters = new Vector();
        fxBCommonParameters.ensureCapacity(2 * 15);
    }

    public InsertFx[] initInsertFxs() {
        for (int i = 0; i < insertFxs.length; i++) {
            insertFxs[i] = new InsertFx.Chorus();
        }
        return insertFxs;
    }

    public BusFx[] initBusFxs() {
        for (int i = 0; i < busFxs.length; i++) {
            busFxs[i] = new BusFx.HallReverb();
        }
        return busFxs;
    }

    /**
     *
     * @param fc
     * @throws IOException
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    public void readCommonParameters(FileChannel fc) throws IOException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        CommonParameter cp;
        while (fc.position() < fc.size() - Constant.SIGNATURE_LENGTH) {
            cp = CommonParameter.CreateFromFileChannel(fc);
            switch(cp.getSignature().charAt(0)) {
                case 'A':
                    this.arps[((ArpCommonParameter) cp).getArpNo()].commonParameters.add(cp);
                    break;
                case 'E':
                    switch(cp.getSignature().charAt(1)) {
                        case 'i':
                            switch(cp.getSignature().charAt(2)) {
                                case 'c':
                                    this.fxCommonParameters.add(cp);
                                    break;
                                default:
                                    this.fxICommonParameters.add(cp);
                            }
                            break;
                        case 'b':
                            this.fxBCommonParameters.add(cp);
                            break;
                        case 'e':
                            this.EQ.commonParameters.add(cp);
                            break;
                        default:
                            this.fxCommonParameters.add(cp);
                    }
                    break;
                case 'T':
                    this.trackCommonParameters.add(cp);
                    break;
                case 'G':
                    this.switches.commonParameters.add(cp);
                    break;
                default:
                    this.commonParameters.add(cp);
            }
        }
    }

    void writeCommonParameters(FileChannel fc) throws IOException {
        for (int i = 0; i < commonParameters.size(); i++) {
            commonParameters.get(i).writeToFileChannel(fc);
        }
        for (int i = 0; i < switches.commonParameters.size(); i++) {
            switches.commonParameters.get(i).writeToFileChannel(fc);
        }
        for (int i = 0; i < fxCommonParameters.size(); i++) {
            fxCommonParameters.get(i).writeToFileChannel(fc);
        }
        for (int e = 0; e < insertFxs.length; e++) {
            insertFxs[e].writeToFileChannel(e, fc);
        }
        for (int b = 0; b < busFxs.length; b++) {
            busFxs[b].writeToFileChannel(b, fc);
        }
        for (int i = 0; i < trackCommonParameters.size(); i++) {
            trackCommonParameters.get(i).writeToFileChannel(fc);
        }
        for (int a = 0; a < arps.length; a++) {
            for (int i = 0; i < arps[a].commonParameters.size(); i++) {
                arps[a].commonParameters.get(i).writeToFileChannel(fc);
            }
        }
    }

    /**
     * 
     * @param cpv
     * @param signature
     * @return
     * @throws ClassNotFoundException
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    protected static Vector<CommonParameter> findCommonParameter(Vector<CommonParameter> cpv, String signature) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        return findCommonParameter(cpv, signature, 0);
    }

    /**
     *
     * @param cpv
     * @param signature
     * @param index
     * @return
     * @throws ClassNotFoundException
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    protected static Vector<CommonParameter> findCommonParameter(Vector<CommonParameter> cpv, String signature, int index) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        Vector<CommonParameter> cpArray = new Vector();
        for (int i = Utility.adjustRange(index, 0, cpv.size() - 1); (i < cpv.size()); i++) {
            if (cpv.get(i).getSignature().equals(signature)) {
                cpArray.add(cpv.get(i));
            }
        }
        return cpArray;
    }

    /**
     *
     * @param cpv
     * @param cpClass
     * @return
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    protected static Vector<CommonParameter> findCommonParameter(Vector<CommonParameter> cpv, Class cpClass) throws InstantiationException, IllegalAccessException {
        return findCommonParameter(cpv, cpClass, 0);
    }

    /**
     *
     * @param cpv
     * @param cpClass
     * @param index
     * @return
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    protected static Vector<CommonParameter> findCommonParameter(Vector<CommonParameter> cpv, Class cpClass, int index) throws InstantiationException, IllegalAccessException {
        Vector<CommonParameter> cpArray = new Vector();
        for (int i = Utility.adjustRange(index, 0, cpv.size() - 1); (i < cpv.size()); i++) {
            if (cpv.get(i).getClass().equals(cpClass)) {
                cpArray.add(cpv.get(i));
            }
        }
        return cpArray;
    }

    /**
     *
     */
    protected void parseCommonParameters() {
        try {
            for (int a = 0; a < arps.length; a++) {
                arps[a].parseArpeggiator();
            }
            EQ.parseEQ();
            switches.parseSwitches();
            parseInsertFx();
            parseBusFx();
        } catch (InstantiationException ex) {
            Logger.getLogger(FusionCommonObject.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(FusionCommonObject.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void parseInsertFx() throws InstantiationException, IllegalAccessException {
        Vector cps = findCommonParameter(this.fxICommonParameters, Eitp.class);
        int index = 0;
        for (int i = 0; i < cps.size(); i++) {
            index = ((Eitp) cps.elementAt(i)).getIndex();
            insertFxs[index] = (InsertFx) Tables.insertFxList[((Eitp) cps.elementAt(i)).getTypeId()].newInstance();
        }
        cps = findCommonParameter(this.fxICommonParameters, Eipm.class);
        for (int i = 0; i < cps.size(); i++) {
            index = ((Eipm) cps.elementAt(i)).getIndex();
            insertFxs[index].params[((Eipm) cps.elementAt(i)).getParamNo()] = ((Eipm) cps.elementAt(i)).getValue();
        }
    }

    private void parseBusFx() throws InstantiationException, IllegalAccessException {
        Vector cps = findCommonParameter(this.fxBCommonParameters, Ebtp.class);
        int index = 0;
        for (int i = 0; i < cps.size(); i++) {
            index = ((Ebtp) cps.elementAt(i)).getIndex();
            busFxs[index] = (BusFx) Tables.busFxList[((Ebtp) cps.elementAt(i)).getTypeId()].newInstance();
        }
        cps = findCommonParameter(this.fxICommonParameters, Ebpm.class);
        for (int i = 0; i < cps.size(); i++) {
            index = ((Ebpm) cps.elementAt(i)).getIndex();
            busFxs[index].params[((Ebpm) cps.elementAt(i)).getParamNo()] = ((Ebpm) cps.elementAt(i)).getValue();
        }
    }
}
