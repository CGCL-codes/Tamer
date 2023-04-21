package tags;

import java.io.*;
import java.lang.System.*;
import java.util.*;
import org.gjt.sp.jedit.View;

/**
 * The tag parser interface. 
 *
 * @author Kenrick Drew
 */
interface TagsParser {

    public void reinitialize();

    /**
   * Finds the tag line in the tag file and returns it.
   *
   * @param tagFileName The tag file path.
   * @param tagToLookFor The tag to look for in the file.
   * @return Vector of tag lines found in the file, null if it couldn't find it. 
   */
    public boolean findTagLines(String tagFileName, String tagToLookFor, View view);

    public TagLine createTagLine(String tagLine, String tagIndexFile);

    /**
   * Returs the number of found tags
   *
   * @return The number of found tags.
   */
    public int getNumberOfFoundTags();

    public Vector getTagLines();

    public ChooseTagList getCollisionListComponent(View view);

    public TagLine getTagLine(int index);

    public String getTag();

    public String toString();
}
