package edu.berkeley.guir.quill;

import java.util.Collection;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.text.Position;

/** 
 * <P>
 * This software is distributed under the 
 * <A HREF="http://guir.cs.berkeley.edu/projects/COPYRIGHT.txt">
 * Berkeley Software License</A>.
 
*/
public interface Notice {

    /** in ms since the epoch (see System.currentTimeMillis) */
    long getLastDisplayTime();

    void setLastDisplayTime(long time);

    /** display the notice if appropriate to do so */
    void display(SummaryLog log);

    /** display a short version of the notice */
    void displaySummary(SummaryLog log);

    /** Return the GestureObject(s) that this notice refers to.  Most
      commonly will be a Set or a List. */
    Collection getGestureObjects();

    /** a short user-intelligible name */
    String getName();

    /** Get the severity of the notice.  1 is purely informational,
      higher numbers are more severe. */
    int getSeverity();

    /** return the icon for this notice. */
    Icon getIcon();

    /** Return the position of this notice in the summary it was last
      displayed in. */
    Position getStartPosition();

    void setStartPosition(Position p);

    Position getEndPosition();

    void setEndPosition(Position p);

    /** get a component that displays information about how the designer
      can address the issues the Notice brings up */
    HowToPanel getHowToPanel();

    /** get a tag into the reference manual for a section related to
      this Notice (e.g., "#Similarity") */
    String getReferenceTag();
}
