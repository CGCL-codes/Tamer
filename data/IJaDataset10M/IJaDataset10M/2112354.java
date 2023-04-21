package edu.georgetown.nnj.data.formats;

import edu.georgetown.nnj.data.NNJDataSource;
import edu.georgetown.nnj.data.NNJDataSourceConsumer;
import edu.georgetown.nnj.events.NNJChangeEvent;
import edu.georgetown.nnj.filterblocks.filters.NNJFilterPassthru;
import java.io.*;

/** Writes the datastream to a file.
 */
public final class NNJDataSaveDa extends NNJFilterPassthru implements NNJDataSourceConsumer {

    public NNJDataSaveDa(NNJDataSource source) {
        super(source);
    }

    public NNJDataSaveDa(NNJDataSource source, boolean filterActive) {
        super(source, filterActive);
    }

    protected NNJDataSaveDa() {
        super();
    }

    @Override
    public void setNNJDataSourceImpl() {
    }

    public void saveDA(String string) throws IOException {
        saveDA(new File(string));
    }

    public void saveDA(File file) throws IOException {
        FileOutputStream fout = new FileOutputStream(file);
        BufferedOutputStream bout = new BufferedOutputStream(fout);
        DataOutputStream dout = new DataOutputStream(bout);
        for (int k = 0; k < 2560; k++) {
        }
        NNJDataSource tempSource = this.getNNJDataSource();
        for (int det = 0; det < this.getDataLayout().getChannelCount(); det++) for (int frame = 0; frame < this.getTotalFrameCount(); frame++) {
            dout.writeShort(Short.reverseBytes((short) ((double) tempSource.readDataPoint(det, frame) / tempSource.getDataExtraBits())));
        }
        dout.close();
        bout.close();
        fout.close();
    }

    @Override
    public void stateChangedNNJImplAllChannels(NNJChangeEvent evt) {
    }

    @Override
    public void stateChangedNNJImplSomeChannels(NNJChangeEvent evt) {
    }

    @Override
    public void stateChangedNNJImplLayout(NNJChangeEvent evt) {
    }

    @Override
    public void stateChangedNNJImplWindow(NNJChangeEvent evt) {
    }

    @Override
    public void stateChangedNNJImplMask(NNJChangeEvent evt) {
    }

    @Override
    public double getSamplingRate() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getAbsoluteUnit() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void stateChangedNNJImplCommonPre(NNJChangeEvent evt) {
    }

    @Override
    public void stateChangedNNJImplCommonPost(NNJChangeEvent evt) {
    }
}
