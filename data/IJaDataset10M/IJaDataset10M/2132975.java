package org.gnf.seqtracs.seqretrival;

import javax.swing.JFrame;
import org.gnf.seqtracs.utilities.Univariate;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */
public class HistogramFrame extends JFrame {

    public HistogramFrame(final Univariate uni, final String trace_file_name, final String trace_file_path) {
        _uni = uni;
        _trace_file_name = trace_file_name;
        _trace_file_path = trace_file_path;
        init();
    }

    void init() {
        setTitle("Trace: " + _trace_file_path + "/" + _trace_file_name);
        HistogramCanvas hc = new HistogramCanvas(600, 400);
        hc.setClassType("Ten");
        double[] l = new double[2];
        l[0] = 0.0;
        if (_uni.max() <= 60) {
            l[1] = 60;
        } else {
            l[1] = 100;
        }
        hc.newData(_uni, l, true);
        hc.addXLabel("Phred Quality Values");
        getContentPane().add(hc);
        setSize((int) hc.getSize().getWidth() + 20, (int) hc.getSize().getHeight() + 40);
        setVisible(true);
    }

    final Univariate _uni;

    final String _trace_file_name, _trace_file_path;
}
