package com.flagstone.transform;

/**
The FSWaitForFrame2 action instructs the player to wait until the specified frame 
number or named frame has been loaded. 
 
<p>If the frame has been loaded then the following <i>n</i> actions are executed. The FSWaitForFrame2 action extends the FSWaitForFrame action by allowing the name of a frame to be specified.</p>

<table class="datasheet">

<tr><th align="left" colspan="2">Attributes</th></tr>

<tr>
<td><a name="FSWaitForFrame2_0">type</a></td>
<td>Identifies the data structure when it is encoded. Read-only.</td>
</tr>

<tr>
<td><a name="FSWaitForFrame2_1">actionCount</a></td>
<td>The number of actions following that will be executed when the frame of interest has been loaded.</td>
</tr>
</table>

<p>FSWaitForFrame2 is a stack-based action. The frame number or frame name which should be loaded to trigger execution of the following actions is popped from the Flash Player's stack.</p>

<h1 class="datasheet">Examples</h1>
The following example instructs the Flash Player to wait until the frame with the name "frame" in a movie clip has been loaded:

<pre>
actions.add(new FSPush("frame"));
actions.add(new FSWaitForFrame2(1));
actions.add(new FSGotoFrame2("frame"));
</pre>

<p>This method of waiting until a frame has been loaded is considered obsolete. Determining the number of frames loaded using the FramesLoaded property of the Flash player in combination with an FSIf action is now the preferred mechanism:</p>

<pre>
FSDoAction actions = new FSDoAction();

actions.add(new FSPush("_root"));
actions.add(FSAction.GetVariable());
actions.add(new FSPush(FSPush.FramesLoaded));
actions.add(FSAction.GetProperty());
actions.add(new FSPush(8));
actions.add(FSAction.Less());
actions.add(new FSIf(-29));

actions.add(new FSGotoFrame(8));
</pre>

<h1 class="datasheet">History</h1>

<p>The FSWaitForFrame2 class represents the ActionWaitForFrame2 action of the Macromedia Flash (SWF) File Format Specification. It was introduced in Flash 4.</p>
 */
public class FSWaitForFrame2 extends FSActionObject {

    private int actionCount = 0;

    /**
     * Construct an FSWaitForFrame2 object, initalizing it with values decoded 
     * from an encoded object.
     * 
     * @param coder an FSCoder containing the binary data.
     */
    public FSWaitForFrame2(FSCoder coder) {
        super(WaitForFrame2);
        decode(coder);
    }

    /** Constructs an FSWaitForFrame2 object with the number of actions to execute if the frame has been loaded.

        @param aNumber the number of actions to execute.
        */
    public FSWaitForFrame2(int aNumber) {
        super(WaitForFrame2);
        setActionCount(aNumber);
    }

    /**
     * Constructs an FSWaitForFrame2 object by copying values from an existing 
     * object.
     *
     * @param obj an FSWaitForFrame2 object.
     */
    public FSWaitForFrame2(FSWaitForFrame2 obj) {
        super(obj);
        actionCount = obj.actionCount;
    }

    /** Gets the number of actions to execute.

        @return the number of actions to execute.
        */
    public int getActionCount() {
        return actionCount;
    }

    /** Sets the number of actions to execute.

        @param aNumber the number of actions to execute.
        */
    public void setActionCount(int aNumber) {
        actionCount = aNumber;
    }

    public boolean equals(Object anObject) {
        boolean result = false;
        if (super.equals(anObject)) result = actionCount == ((FSWaitForFrame2) anObject).getActionCount();
        return result;
    }

    public void appendDescription(StringBuffer buffer, int depth) {
        buffer.append(name());
        if (depth > 0) {
            buffer.append(": { ");
            Transform.append(buffer, "actionCount", actionCount);
            buffer.append("}");
        }
    }

    public int length(FSCoder coder) {
        super.length(coder);
        length += 1;
        return length;
    }

    public void encode(FSCoder coder) {
        super.encode(coder);
        coder.writeWord(actionCount, 1);
        coder.endObject(name());
    }

    public void decode(FSCoder coder) {
        super.decode(coder);
        actionCount = coder.readWord(1, false);
        coder.endObject(name());
    }
}
