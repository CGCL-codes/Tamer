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


Class:			AOPan
Autor:			olivier g�umann, neuch�tel (switzerland)
JDK:				1.3

Desctription:	pan operation

History:
Date:			Description:									Autor:
02.06.01		first draft										oli4
17.12.01		add different modes, order and 
				constant pan									oli4

***********************************************************/
public class AOPan extends AOperation {

    public AOPan(int mode, int shape) {
        super();
        this.mode = mode;
        this.shape = shape;
    }

    public AOPan(int mode, int shape, float pan) {
        this(mode, shape);
        this.pan = pan;
    }

    public static final int HALF_MODE = 1;

    public static final int MIX_ENDS_MODE = 2;

    public static final int FULL_MODE = 3;

    private int mode;

    public static final int SQUARE_ROOT_SHAPE = -1;

    public static final int LINEAR_SHAPE = 1;

    public static final int SQUARE_SHAPE = 2;

    private int shape;

    private float pan;

    private float f11, f22, f12, f21;

    private void calculateMatrix(float pan) {
        switch(mode) {
            case MIX_ENDS_MODE:
                f22 = Math.min(1, pan * 2 - 2);
                f11 = Math.min(1, -pan * 2 + 4);
                f12 = 1 - f11;
                f21 = 1 - f22;
                break;
            case FULL_MODE:
                f22 = pan - 1;
                f11 = -pan + 2;
                f12 = 0;
                f21 = 0;
                break;
            default:
                f22 = Math.min(1, pan * 2 - 2);
                f11 = Math.min(1, -pan * 2 + 4);
                f12 = 0;
                f21 = 0;
                break;
        }
        switch(shape) {
            case SQUARE_ROOT_SHAPE:
                f11 = (float) Math.sqrt(f11);
                f22 = (float) Math.sqrt(f22);
                f12 = (float) Math.sqrt(f12);
                f21 = (float) Math.sqrt(f21);
                break;
            case SQUARE_SHAPE:
                f11 *= f11;
                f22 *= f22;
                f12 *= f12;
                f21 *= f21;
                break;
            default:
                break;
        }
    }

    /**
	*	performs constant pan on channel1 and channel2, taking into account
	*	only selection-range of channel1.
	*	value:	1	= channel1 
	*				1.5= center
	*				2	= channel2
	*/
    public void operate(AChannelSelection channel1, AChannelSelection channel2) {
        MMArray s1 = channel1.getChannel().getSamples();
        MMArray s2 = channel2.getChannel().getSamples();
        int o1 = channel1.getOffset();
        int l1 = channel1.getLength();
        float ch1, ch2;
        channel1.getChannel().markChange();
        channel2.getChannel().markChange();
        try {
            calculateMatrix(pan);
            LProgressViewer.getInstance().entrySubProgress(0.7);
            for (int i = 0; i < l1; i++) {
                if (LProgressViewer.getInstance().setProgress((i + 1) * 1.0 / l1)) return;
                ch1 = f11 * s1.get(o1 + i) + f21 * s2.get(o1 + i);
                ch2 = f22 * s2.get(o1 + i) + f12 * s1.get(o1 + i);
                s1.set(i + o1, channel1.mixIntensity(i + o1, s1.get(i + o1), ch1));
                s2.set(i + o1, channel1.mixIntensity(i + o1, s2.get(i + o1), ch2));
            }
            LProgressViewer.getInstance().exitSubProgress();
        } catch (ArrayIndexOutOfBoundsException oob) {
        }
    }

    /**
	*	performs variable pan on channel1 and channel2, taking into account 
	*	only selection-range of channel1.
	*	value:	1	= channel1 
	*				1.5= center
	*				2	= channel2
	*/
    public void operate(AChannelSelection channel1, AChannelSelection channel2, AChannelSelection pan) {
        MMArray s1 = channel1.getChannel().getSamples();
        MMArray s2 = channel2.getChannel().getSamples();
        MMArray p = pan.getChannel().getSamples();
        int o1 = channel1.getOffset();
        int l1 = channel1.getLength();
        float ch1, ch2;
        channel1.getChannel().markChange();
        channel2.getChannel().markChange();
        try {
            LProgressViewer.getInstance().entrySubProgress(0.7);
            for (int i = 0; i < l1; i++) {
                if (LProgressViewer.getInstance().setProgress((i + 1) * 1.0 / l1)) return;
                calculateMatrix(p.get(o1 + i));
                ch1 = f11 * s1.get(o1 + i) + f21 * s2.get(o1 + i);
                ch2 = f22 * s2.get(o1 + i) + f12 * s1.get(o1 + i);
                s1.set(i + o1, channel1.mixIntensity(i + o1, s1.get(i + o1), ch1));
                s2.set(i + o1, channel1.mixIntensity(i + o1, s2.get(i + o1), ch2));
            }
            LProgressViewer.getInstance().exitSubProgress();
        } catch (ArrayIndexOutOfBoundsException oob) {
        }
    }
}
