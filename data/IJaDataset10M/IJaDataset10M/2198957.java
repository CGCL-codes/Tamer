package nz.ac.waikato.modeljunit.examples.gsm;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import nz.ac.waikato.modeljunit.Action;
import nz.ac.waikato.modeljunit.FsmModel;
import nz.ac.waikato.modeljunit.GraphListener;
import nz.ac.waikato.modeljunit.GreedyTester;
import nz.ac.waikato.modeljunit.RandomTester;

/** This is an EFSM model of the SIM card within a mobile phone.
 *  It models just a small part of the functionality to do with
 *  accessing files and directories within the SIM card, and the
 *  PINs and PUKs used to make this secure.
 *
 *  See Chapter 9 of the PMBT book ("Practical Model-Based Testing:
 *  A Tools Approach" by Utting and Legeard, Morgan and Kaufmann 2007)
 *  for more discussion of this system and a version of this model in B.
 *
 *  This ModelJUnit version of the model was translated from a UML/OCL
 *  model developed at LEIRIOS Technologies (now Smartesting.com).
 *  A subset version of this model (
 *  which in turn was adapted from an earlier and larger version of the B model published in
 *  the above book.
 *
 * @author marku
 *
 */
public class SimCard implements FsmModel {

    public enum E_Status {

        Enabled, Disabled
    }

    ;

    public enum B_Status {

        Blocked, Unblocked
    }

    ;

    public enum Status_Word {

        sw_9000, sw_9404, sw_9405, sw_9804, sw_9840, sw_9808, sw_9400
    }

    ;

    public enum File_Type {

        Type_DF, Type_EF
    }

    ;

    public enum Permission {

        Always, CHV, Never, Adm, None
    }

    ;

    /** Names of files on the SIM - or we could use strings for these */
    public enum F_Name {

        MF, DF_GSM, EF_LP, EF_IMSI, DF_Roaming, EF_FR, EF_UK
    }

    ;

    protected static final int GOOD_PUK = 12233445;

    public static final int Max_Pin_Try = 3;

    public static final int Max_Puk_Try = 10;

    /** This models all the files on the SIM and their contents */
    protected Map<F_Name, SimFile> files = new HashMap<F_Name, SimFile>();

    /** The currently-selected directory (never null) */
    protected SimFile DF;

    /** The current elementary file, or null if none is selected */
    protected SimFile EF;

    /** The correct PIN (can be 11 or 12) */
    protected int PIN;

    /** Say whether PIN-checking is Enabled or Disabled */
    protected E_Status status_en;

    /** Number of bad PIN attempts: 0 .. Max_Pin_Try */
    protected int counter_PIN_try;

    /** True means a correct PIN has been entered in this session */
    protected boolean perm_session;

    /** Set to Blocked after too many incorrect PIN attempts */
    protected B_Status status_PIN_block;

    /** Number of bad PUK attempts: 0 .. Max_Puk_Try */
    protected int counter_PUK_try;

    /** Set to Blocked after too many incorrect PUK attempts */
    protected B_Status status_PUK_block;

    /** The status word returned by each command */
    protected Status_Word result;

    /** The data returned by the Read_Binary command */
    protected String read_data;

    /** If this is non-null, each action calls the corresponding adaptor
   *  action, which tests the GSM11 implementation.
   */
    protected SimCardAdaptor sut = null;

    /** If <code>sut0</code> is null, then the model runs without
   *  testing any SUT.
   *
   * @param sut0  Can be null.
   */
    public SimCard(SimCardAdaptor sut0) {
        reset(false);
        sut = sut0;
    }

    /**
   * This sets up a hierarchy of files that is used for testing.
   * Their contents remain constant throughout the testing,
   * but if we extended the model with a Write_Binary action,
   * then the file contents could change during testing.
   */
    private void initFiles() {
        files.clear();
        SimFile mf = new SimFile(File_Type.Type_DF, F_Name.MF, "d1", Permission.None, null);
        SimFile df_gsm = new SimFile(File_Type.Type_DF, F_Name.DF_GSM, "d1", Permission.None, mf);
        SimFile ef_imsi = new SimFile(File_Type.Type_EF, F_Name.EF_IMSI, "d2", Permission.CHV, df_gsm);
        SimFile ef_lp = new SimFile(File_Type.Type_EF, F_Name.EF_LP, "d1", Permission.Always, df_gsm);
        SimFile df_roam = new SimFile(File_Type.Type_DF, F_Name.DF_Roaming, "d1", Permission.None, df_gsm);
        SimFile ef_fr = new SimFile(File_Type.Type_EF, F_Name.EF_FR, "d3", Permission.Adm, df_roam);
        SimFile ef_uk = new SimFile(File_Type.Type_EF, F_Name.EF_UK, "d4", Permission.Never, df_roam);
        files.put(F_Name.MF, mf);
        files.put(F_Name.DF_GSM, df_gsm);
        files.put(F_Name.EF_IMSI, ef_imsi);
        files.put(F_Name.EF_LP, ef_lp);
        files.put(F_Name.DF_Roaming, df_roam);
        files.put(F_Name.EF_FR, ef_fr);
        files.put(F_Name.EF_UK, ef_uk);
    }

    public Object getState() {
        return DF.name.toString() + "," + (EF == null ? "null" : EF.name.toString()) + ",PIN=" + PIN + "," + counter_PIN_try + "," + counter_PUK_try + "," + status_en + "," + (status_PIN_block == B_Status.Blocked ? "PINBLOCKED" : "") + "," + (status_PUK_block == B_Status.Blocked ? "PUKBLOCKED" : "");
    }

    /**
   * This models the creation of a new SIM card.
   */
    public void reset(boolean testing) {
        initFiles();
        PIN = 11;
        status_en = E_Status.Enabled;
        status_PIN_block = B_Status.Unblocked;
        status_PUK_block = B_Status.Unblocked;
        counter_PIN_try = 0;
        counter_PUK_try = 0;
        perm_session = false;
        read_data = "";
        DF = files.get(F_Name.MF);
        EF = null;
        result = Status_Word.sw_9000;
        if (sut != null) {
            sut.reset();
        }
    }

    @Action
    public void verifyPIN11() {
        Verify_PIN(11);
    }

    @Action
    public void verifyPIN12() {
        Verify_PIN(12);
    }

    /**
   * User enters a PIN number.
   * This is called VERIFY_CHV(code) in the PBMT book.
   * 
   * @param pin A 4 digit PIN number.
   */
    public void Verify_PIN(int pin) {
        if (status_PIN_block == B_Status.Blocked) {
            result = Status_Word.sw_9840;
        } else if (status_en == E_Status.Disabled) {
            result = Status_Word.sw_9808;
        } else if (pin == PIN) {
            counter_PIN_try = 0;
            perm_session = true;
            result = Status_Word.sw_9000;
        } else if (counter_PIN_try == Max_Pin_Try - 1) {
            counter_PIN_try = Max_Pin_Try;
            status_PIN_block = B_Status.Blocked;
            perm_session = false;
            result = Status_Word.sw_9840;
            System.out.println("PIN blocked in Verify_PIN!");
        } else {
            counter_PIN_try = counter_PIN_try + 1;
            result = Status_Word.sw_9804;
        }
        if (sut != null) {
            sut.Verify_PIN(pin, result);
        }
    }

    @Action
    public void unblockPINGood12() {
        Unblock_PIN(GOOD_PUK, 12);
    }

    @Action
    public void unblockPINBad() {
        Unblock_PIN(12233446, 11);
    }

    /**
   * User enters a PUK code to try and unblock a blocked PIN.
   * This is called UNBLOCK_CHV(code_unblock, new_code) in the PMBT book.
   *
   * @param puk The Personal Unblock Key code (eight digits).
   * @param newPin The new PIN code to use if the unblock is successful.
   */
    public void Unblock_PIN(int puk, int newPin) {
        if (status_PUK_block == B_Status.Blocked) {
            result = Status_Word.sw_9840;
        } else if (puk == GOOD_PUK) {
            PIN = newPin;
            counter_PIN_try = 0;
            counter_PUK_try = 0;
            perm_session = true;
            status_PIN_block = B_Status.Unblocked;
            result = Status_Word.sw_9000;
            if (status_en == E_Status.Disabled) {
                status_en = E_Status.Enabled;
                System.out.println("PIN unblocked, setting status Enabled");
            } else {
                System.out.println("PIN unblocked, leaving status Enabled");
            }
        } else if (counter_PUK_try == Max_Puk_Try - 1) {
            counter_PUK_try = Max_Puk_Try;
            status_PUK_block = B_Status.Blocked;
            perm_session = false;
            result = Status_Word.sw_9840;
            System.out.println("PUK BLOCKED!!! (after " + counter_PUK_try + " bad attempts)");
        } else {
            counter_PUK_try = counter_PUK_try + 1;
            result = Status_Word.sw_9804;
        }
        if (sut != null) {
            sut.Unblock_PIN(puk, newPin, result);
        }
    }

    @Action
    public void enablePIN11() {
        Enabled_PIN(11);
    }

    /**
   * This turns PIN checking on.
   * This operation is not included in the model in the PMBT book.
   *
   * @param pin User must supply the correct PIN to enable PIN checking!
   */
    public void Enabled_PIN(int pin) {
        if (status_PIN_block == B_Status.Blocked) {
            result = Status_Word.sw_9840;
        } else if (status_en == E_Status.Enabled) {
            result = Status_Word.sw_9808;
        } else if (pin == PIN) {
            counter_PIN_try = 0;
            perm_session = true;
            status_en = E_Status.Enabled;
            result = Status_Word.sw_9000;
        } else if (counter_PIN_try == Max_Pin_Try - 1) {
            counter_PIN_try = Max_Pin_Try;
            status_PIN_block = B_Status.Blocked;
            perm_session = false;
            result = Status_Word.sw_9840;
            System.out.println("PIN blocked in Enabled_PIN!");
        } else {
            counter_PIN_try = counter_PIN_try + 1;
            result = Status_Word.sw_9804;
        }
        if (sut != null) {
            sut.Enabled_PIN(pin, result);
        }
    }

    @Action
    public void disablePINGood() {
        Disabled_PIN(11);
    }

    /**
   * This turns PIN checking off.
   * This operation is not included in the model in the PMBT book.
   *
   * @param pin User must supply the correct PIN to disable PIN checking.
   */
    public void Disabled_PIN(int pin) {
        if (status_PIN_block == B_Status.Blocked) {
            result = Status_Word.sw_9840;
        } else if (status_en == E_Status.Disabled) {
            result = Status_Word.sw_9808;
        } else if (pin == PIN) {
            counter_PIN_try = 0;
            perm_session = true;
            status_en = E_Status.Disabled;
            result = Status_Word.sw_9000;
        } else if (counter_PIN_try == Max_Pin_Try - 1) {
            counter_PIN_try = Max_Pin_Try;
            status_PIN_block = B_Status.Blocked;
            perm_session = false;
            result = Status_Word.sw_9840;
            System.out.println("PIN blocked in Disabled_PIN!");
        } else {
            counter_PIN_try = counter_PIN_try + 1;
            result = Status_Word.sw_9804;
        }
        if (sut != null) {
            sut.Disabled_PIN(pin, result);
        }
    }

    @Action
    public void changePinSame() {
        Change_PIN(11, 11);
    }

    @Action
    public void changePinNew() {
        Change_PIN(11, 12);
    }

    /**
   * Changes the current PIN number to a new PIN number.
   * This operation is not included in the model in the PMBT book
   * (in that model, UNBLOCK_CHV is the only way of changing the PIN).
   *
   * @param oldPin Current PIN number
   * @param newPin New four digit PIN code.
   */
    public void Change_PIN(int oldPin, int newPin) {
        if (status_PIN_block == B_Status.Blocked) {
            result = Status_Word.sw_9840;
        } else if (status_en == E_Status.Disabled) {
            result = Status_Word.sw_9808;
        } else if (oldPin == PIN) {
            PIN = newPin;
            counter_PIN_try = 0;
            perm_session = true;
            result = Status_Word.sw_9000;
        } else if (counter_PIN_try == Max_Pin_Try - 1) {
            counter_PIN_try = Max_Pin_Try;
            status_PIN_block = B_Status.Blocked;
            perm_session = false;
            result = Status_Word.sw_9840;
            System.out.println("PIN blocked in Change_PIN!");
        } else {
            counter_PIN_try = counter_PIN_try + 1;
            result = Status_Word.sw_9804;
        }
        if (sut != null) {
            sut.Change_PIN(oldPin, newPin, result);
        }
    }

    @Action
    public void selectMF() {
        Select_file(F_Name.MF);
    }

    @Action
    public void selectDF_Gsm() {
        Select_file(F_Name.DF_GSM);
    }

    @Action
    public void selectDF_Roaming() {
        Select_file(F_Name.DF_Roaming);
    }

    @Action
    public void selectEF_IMSI() {
        Select_file(F_Name.EF_IMSI);
    }

    @Action
    public void selectEF_LP() {
        Select_file(F_Name.EF_LP);
    }

    @Action
    public void selectEF_FR() {
        Select_file(F_Name.EF_FR);
    }

    /**
   * User selects a current directory or a current file.
   * This is called SELECT_FILE(ff) in the model in the PMBT book.
   * 
   * @param filename The desired new current directory/file.
   */
    public void Select_file(F_Name filename) {
        SimFile temp_file = files.get(filename);
        if (temp_file.type == File_Type.Type_DF) {
            if (filename == F_Name.MF || filename == DF.name || temp_file == DF.parent || (temp_file.parent != null && temp_file.parent == DF)) {
                result = Status_Word.sw_9000;
                DF = temp_file;
                EF = null;
            } else {
                result = Status_Word.sw_9404;
            }
        } else {
            if (temp_file.parent == DF) {
                result = Status_Word.sw_9000;
                EF = temp_file;
            } else {
                result = Status_Word.sw_9405;
            }
        }
        if (sut != null) {
            sut.Select_file(filename, result);
        }
    }

    /**
   * Read the contents of the current elementary file (if any).
   * This is called READ_BINARY in the model in the PMBT book.
   */
    @Action
    public void Read_Binary() {
        if (EF == null) {
            result = Status_Word.sw_9400;
        } else if (EF.perm_read == Permission.Always) {
            result = Status_Word.sw_9000;
            read_data = EF.data;
        } else if (EF.perm_read == Permission.Never) {
            result = Status_Word.sw_9804;
        } else if (EF.perm_read == Permission.Adm) {
            result = Status_Word.sw_9804;
        } else if (perm_session) {
            result = Status_Word.sw_9000;
            read_data = EF.data;
        } else {
            result = Status_Word.sw_9804;
        }
        if (sut != null) {
            sut.Read_Binary(result, read_data);
        }
    }

    public static void main(String[] args) throws FileNotFoundException {
        System.out.println("Exploring the SimCard model without testing the SUT.");
        RandomTester tester = new GreedyTester(new SimCard(null));
        tester.setResetProbability(0.0001);
        GraphListener graph = tester.buildGraph(1000000);
        graph.printGraphDot("gsm.dot");
        System.out.println("Graph contains " + graph.getGraph().numVertices() + " states and " + graph.getGraph().numEdges() + " transitions.");
    }
}
