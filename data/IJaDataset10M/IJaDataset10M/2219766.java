package ch.laoe.operation;

import ch.laoe.clip.AChannelSelection;
import ch.laoe.clip.MMArray;
import ch.laoe.ui.Debug;
import ch.laoe.ui.LProgressViewer;

/***********************************************************

This file is part of LAoE.

LAoE is free software; you can redistribute it and/or modify it
under the terms of the GNU General Public License as published
by the Free Software Foundation; either version 2 of the License,
or (at your option) any later version.

LAoE is distributed in the hope that it will be useful, but
WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with LAoE; if not, write to the Free Software Foundation,
Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA


Class:			AOMultiPitch
Autor:			olivier g�umann, neuch�tel (switzerland)
JDK:				1.3

Desctription:	multi-pitch operation 

History:
Date:			Description:									Autor:
19.06.01		first draft										oli4

***********************************************************/
public class AOMultiPitch extends AOperation implements AOFftBlockProcessing.Processor {

    public AOMultiPitch(float[] pitch, float[] amplitude, float dry, float wet, int fftLength, int windowType, float overlap) {
        blockProcessing = new AOFftBlockProcessing(this);
        blockProcessing.setFftBlockLength(fftLength);
        blockProcessing.setFftWindowType(windowType);
        blockProcessing.setOverlapFactor(overlap);
        blockProcessing.setRmsAdaptionEnabled(true);
        blockProcessing.setZeroCrossEnabled(false);
        this.pitch = pitch;
        this.amplitude = amplitude;
        tmpRe = new MMArray(fftLength / 2, 0);
        tmpIm = new MMArray(fftLength / 2, 0);
    }

    private AOFftBlockProcessing blockProcessing;

    private MMArray tmpRe, tmpIm;

    private float pitch[], amplitude[];

    public void process(MMArray re, MMArray im, int x, int length) {
        tmpRe.clear();
        tmpIm.clear();
        for (int j = 0; j < pitch.length; j++) {
            if (pitch[j] > 0) {
                for (int k = 0; k < length; k++) {
                    float shIndex = ((float) k) / pitch[j];
                    if (shIndex < tmpRe.getLength()) {
                        tmpRe.set(k, tmpRe.get(k) + AOToolkit.interpolate3(re, shIndex) * amplitude[j]);
                    }
                    if (shIndex < tmpIm.getLength()) {
                        tmpIm.set(k, tmpIm.get(k) + AOToolkit.interpolate3(im, shIndex) * amplitude[j]);
                    }
                }
            }
        }
        re.copy(tmpRe, 0, 0, tmpRe.getLength());
        im.copy(tmpIm, 0, 0, tmpIm.getLength());
    }

    public final void operate(AChannelSelection chs) {
        blockProcessing.operate(chs);
    }
}
