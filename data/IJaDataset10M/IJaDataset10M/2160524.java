package Sale.Display;

/**
  * Abstract superclass of all sub menu sheets.
  *
  * <p>A SubmenuSheet is a MenuSheet that can be displayed from another MenuSheet via a button.</p>
  *
  * <p><strong>Attention:</strong>The display()-method of SubmenuSheets is required to return
  * <b>only</b> when the SubmenuSheet was hidden.</p>
  *
  * @see MenuSheet#display
  *
  * @version 0.5
  * @author Steffen Zschaler
  */
public abstract class SubmenuSheet extends MenuSheet {

    /**
    * Create a new SubmenuSheet.
    *
    * @param fJoinPerformThread if true selectButtonAt will wait for the action to be performed.
    *
    * @see #selectButtonAt
    */
    SubmenuSheet(DisplayManager dm, boolean fJoinPerformThread) {
        super(dm, fJoinPerformThread);
    }

    /**
    * Add a button to the MenuSheet that redisplays the parent MenuSheet.
    */
    public void addBackButton(String name) {
        addButton(name, new ButtonAction() {

            public void perform() {
                hide();
            }
        });
    }
}
