package sidekick.java;

import java.util.List;
import org.gjt.sp.jedit.View;

/**
 * This class provides a completion popup used solely for inserting imports
 */
public class JavaImportCompletion extends JavaCompletion {

    public JavaImportCompletion(View view, String text, List choices) {
        super(view, text, choices);
    }

    public List getChoices() {
        return super.items;
    }

    public void insert(int index) {
        insertImport(textArea, String.valueOf(get(index)));
    }
}
