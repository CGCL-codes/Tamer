package corina.cross;

import corina.Sample;
import corina.core.App;
import corina.ui.I18n;

/**
 <p>A (Student's) T-Score crossdate.</p>

 <p>This algorithm is based on Mecki Pohl's algorithm, and gives
 very similar results (reason for deviations unknown, possibly
 rounding error).  I'm not completely certain this implementation is
 correct, but it gives good results.</p>

 <p>There are apparently many (dozens of?) T-score algorithms in
 existance.  This one is taken from Baillie and Pilcher's "A Simple
 Crossdating Program", pp. 7-14, Tree-Ring Bulletin, Vol. 33, 1973.
 (That version was "written in FORTRAN IV and uses a card reader and
 line printer".)</p>

 <p>The procedure used is as follows:</p>

 <ol>

 <li>Normalize the data (make it "bivariate-normal"):</li>

 <ul>

 <li>convert each value to the percentage of the mean of the 5
 values it is the center of; then</li>

 <li>take the natural logarithm of each value</li>

 </ul>

 <li>For each possible overlap, compute the r (correlation coefficient):</li>

 <blockquote>
 <i>s</i><sub>1</sub> = <big>&Sigma;</big>
 ( <i>x</i><sub>i</sub><i>y</i><sub>i</sub> -
 <i>N</i> ( <i>x</i><sub>i</sub> - <i>x</i><sub>avg</sub> )
 ( <i>y</i><sub>i</sub> - <i>y</i><sub>avg</sub> ) )
 </blockquote>

 <blockquote>
 <i>s</i><sub>2</sub> = <big>&Sigma;</big>
 ( <i>x</i><sub>i</sub><sup>2</sup> -
 <i>N</i> ( <i>x</i><sub>i</sub> - <i>x</i><sub>avg</sub> )<sup>2</sup> )
 </blockquote>

 <blockquote>
 <i>s</i><sub>3</sub> = <big>&Sigma;</big>
 ( <i>y</i><sub>i</sub><sup>2</sup> -
 <i>N</i> ( <i>y</i><sub>i</sub> - <i>y</i><sub>avg</sub> )<sup>2</sup> )
 </blockquote>

 <blockquote>
 <i>r</i> = <i>s</i><sub>1</sub> / &radic;( <i>s</i><sub>2</sub> <i>s</i><sub>3</sub> )
 </blockquote>

 <li>Compute the t-score:</li>

 <blockquote>
 <i>t</i> = <i>r</i> &radic;( (<i>N</i> - 2) / (1 - <i>r</i><sup>2</sup>) )
 </blockquote>
 
 </ol>

 <p>The work of computing the r-value is independent, and it turns
 out users want that statistic, too, so that has been extracted into
 the class RValue, which TScore now extends.</p>

 <p>This class is dedicated to poor Mr. Potter out in Van Nuys.</p>

 @author Ken Harris &lt;kbh7 <i style="color: gray">at</i> cornell <i style="color: gray">dot</i> edu&gt;
 @version $Id: TScore.java,v 1.8 2006/02/24 20:51:34 lucasmo Exp $ */
public class TScore extends RValue {

    protected TScore() {
    }

    /** Construct a new T-score from two samples.
	 @param s1 the fixed sample
	 @param s2 the moving sample */
    public TScore(Sample s1, Sample s2) {
        super(s1, s2);
    }

    /** Return a prettier name for this cross: "T-Score".
	 @return the name of this cross, "T-Score" */
    public String getName() {
        return I18n.getText("tscore");
    }

    /** A format string for T-scores.
	 @return a format string for T-scores */
    public String getFormat() {
        return App.prefs.getPref("corina.cross.tscore.format", "0.00");
    }

    private static float table[] = { 63.657f, 9.925f, 5.841f, 4.604f, 4.032f, 3.707f, 3.499f, 3.355f, 3.250f, 3.169f, 3.106f, 3.055f, 3.012f, 2.977f, 2.947f, 2.921f, 2.898f, 2.878f, 2.861f, 2.845f, 2.831f, 2.819f, 2.807f, 2.797f, 2.787f, 2.779f, 2.771f, 2.763f, 2.756f, 2.750f };

    public boolean isSignificant(float score, int overlap) {
        if (overlap == 0) return false;
        float threshold = ((overlap <= 30) ? table[overlap - 1] : 2.576f);
        return (score >= threshold);
    }

    public float getMinimumSignificant() {
        return 2.55f;
    }

    /**
	 Given offsets into the fixed and moving data, compute a single
	 T-score for that position.

	 @return the T-score for this possible cross
	 */
    public float compute(int offsetFixed, int offsetMoving) {
        int overlap = Math.min(getFixed().data.size() - offsetFixed, getMoving().data.size() - offsetMoving);
        float r = super.compute(offsetFixed, offsetMoving);
        if (r < 0) return 0;
        float num = (float) Math.sqrt(overlap - 2);
        float den = (float) Math.sqrt(1 - r * r);
        float t = r * num / den;
        if (Float.isNaN(t)) t = 0;
        return t;
    }
}
