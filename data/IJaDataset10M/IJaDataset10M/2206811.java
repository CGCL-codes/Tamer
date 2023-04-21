package org.targol.warfocdamanager.ui.dragndrop;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.eclipse.swt.dnd.ByteArrayTransfer;
import org.eclipse.swt.dnd.TransferData;
import org.targol.warfocdamanager.core.model.SlotType;
import org.targol.warfocdamanager.core.model.cda.BuildProgress;
import org.targol.warfocdamanager.core.model.cda.CdaUnit;
import org.targol.warfocdamanager.core.utils.GamesTypesFactory;

/**
 * Class for serializing gadgets to/from a byte array.
 */
public final class CdaUnitTransfer extends ByteArrayTransfer {

    private static CdaUnitTransfer instance = new CdaUnitTransfer();

    private static final String TYPE_NAME = "CdaUnit-transfer-format";

    private static final int TYPEID = registerType(TYPE_NAME);

    /**
	 * Returns the singleton instance.
	 * 
	 * @return the singleton instance.
	 */
    public static CdaUnitTransfer getInstance() {
        return instance;
    }

    /**
	 * Avoid explicit instantiation.
	 */
    private CdaUnitTransfer() {
    }

    private CdaUnit[] fromByteArray(final byte[] bytes) {
        final DataInputStream in = new DataInputStream(new ByteArrayInputStream(bytes));
        try {
            final int n = in.readInt();
            final CdaUnit[] gadgets = new CdaUnit[n];
            for (int i = 0; i < n; i++) {
                final CdaUnit unit = readUnit(in);
                if (unit == null) {
                    return null;
                }
                gadgets[i] = unit;
            }
            return gadgets;
        } catch (final IOException e) {
            return null;
        }
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    protected int[] getTypeIds() {
        return new int[] { TYPEID };
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    protected String[] getTypeNames() {
        return new String[] { TYPE_NAME };
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    protected void javaToNative(final Object object, final TransferData transferData) {
        final byte[] bytes = toByteArray((CdaUnit[]) object);
        if (bytes != null) {
            super.javaToNative(bytes, transferData);
        }
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public Object nativeToJava(final TransferData transferData) {
        final byte[] bytes = (byte[]) super.nativeToJava(transferData);
        return fromByteArray(bytes);
    }

    /**
	 * Converts given units as a Byte Array.
	 * 
	 * @param units the units to be converted.
	 * @return converted units.
	 */
    byte[] toByteArray(final CdaUnit[] units) {
        final ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        final DataOutputStream out = new DataOutputStream(byteOut);
        byte[] bytes = null;
        try {
            out.writeInt(units.length);
            for (final CdaUnit node : units) {
                writeUnit(node, out);
            }
            out.close();
            bytes = byteOut.toByteArray();
        } catch (final IOException e) {
            e.printStackTrace();
        }
        return bytes;
    }

    /**
	 * Reads and returns a single TreeObject from the given stream. TreeObject serialization format is as follows: <li>
	 * (String) name of TreeObject</li> <li>(NodeInfos) infos on TreeObject</li> <li>(int) number of child nodes</li>
	 * <li>(CdaUnit) child 1 ... repeat for each child</li>
	 */
    private CdaUnit readUnit(final DataInputStream dataIn) throws IOException {
        final SlotType slot = GamesTypesFactory.getSlotWithName(dataIn.readUTF());
        final String name = dataIn.readUTF();
        final float cost = dataIn.readFloat();
        final int nbFigs = dataIn.readInt();
        final int nbStages = dataIn.readInt();
        final String[] stages = new String[nbStages];
        for (int i = 0; i < nbStages; i++) {
            stages[i] = dataIn.readUTF();
            final List<BuildProgress> progresses = new ArrayList<BuildProgress>(nbFigs);
            for (int j = 0; j < nbFigs; j++) {
                progresses.add(BuildProgress.getValueFromLabel(dataIn.readUTF()));
            }
        }
        final CdaUnit newParent = new CdaUnit(slot, name, nbFigs, cost, stages);
        return newParent;
    }

    /**
	 * Writes the given TreeObject to the stream. TreeObject serialization format is as follows: <li>(String) name of
	 * TreeObject</li> <li>(NodeInfos) infos on TreeObject</li> <li>(int) number of child nodes</li> <li>(CdaUnit) child
	 * 1 ... repeat for each child</li>
	 */
    private void writeUnit(final CdaUnit unit, final DataOutputStream dataOut) throws IOException {
        dataOut.writeUTF(unit.getSlot().getName());
        dataOut.writeUTF(unit.getName());
        dataOut.writeFloat(unit.getIndividualCost());
        dataOut.writeInt(unit.getNbFigs());
        final Map<String, List<BuildProgress>> progress = unit.getProgresses();
        dataOut.writeInt(progress.size());
        for (final String stage : progress.keySet()) {
            dataOut.writeUTF(stage);
            final List<BuildProgress> progresses = progress.get(stage);
            for (final BuildProgress prog : progresses) {
                dataOut.writeUTF(prog.getLabel());
            }
        }
    }
}
