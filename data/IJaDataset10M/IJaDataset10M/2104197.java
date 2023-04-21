package ch.laoe.operation;

import ch.laoe.clip.AChannelSelection;
import ch.laoe.clip.MMArray;
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


Class:			AOSaturate
Autor:			olivier g�umann, neuch�tel (switzerland)
JDK:				1.3

Desctription:	saturates selections to the defined max-value.

History:
Date:			Description:									Autor:
29.07.00		erster Entwurf									oli4
04.08.00		neuer Stil        							oli4
24.10.00		neuer Stil        							oli4
19.12.00		float audio samples							oli4
26.01.01		array-based again...							oli4

***********************************************************/
public class AOSaturate extends AOperation {

    public AOSaturate(float maxValue) {
        this.maxValue = maxValue;
    }

    private float maxValue;

    public void operate(AChannelSelection ch1) {
        MMArray s1 = ch1.getChannel().getSamples();
        int o1 = ch1.getOffset();
        int l1 = ch1.getLength();
        ch1.getChannel().markChange();
        LProgressViewer.getInstance().entrySubProgress(0.7);
        for (int i = o1; i < (o1 + l1); i++) {
            if (LProgressViewer.getInstance().setProgress((i + 1 - o1) * 1.0 / l1)) return;
            float s = s1.get(i);
            if (s > maxValue) s = maxValue;
            if (s < -maxValue) s = -maxValue;
            s1.set(i, ch1.mixIntensity(i, s1.get(i), s));
        }
        LProgressViewer.getInstance().exitSubProgress();
    }
}
