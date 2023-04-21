package com.flagstone.transform;

/**
The FSWaitForFrame action instructs the player to wait until the specified frame 
 number has been loaded. 
 
<p>If the frame has been loaded then the actions in the following <i>n</i> actions are executed. This action is most often used to execute a short animation loop that plays until the main part of a movie has been loaded.</p>

<table class="datasheet">

<tr><th align="left" colspan="2">Attributes</th></tr>

<tr>
<td><a name="FSWaitForFrame_0">type</a></td>
<td>Identifies the action when it is encoded. Read-only.</td>
</tr>

<tr>
<td><a name="FSWaitForFrame_1">frameNumber</a></td>
<td>The number of the frame to check to see whether it has been loaded.</td>
</tr>

<tr>
<td><a name="FSWaitForFrame_2">actionCount</a></td>
<td>The number of actions, following the FSWaitForFrame action that will be executed when the frame has been loaded.</td>
</tr>
</table>

<h1 class="datasheet">Examples</h1>
The following example instructs the Flash Player to wait until the eighth frame of a movie clip has been loaded then to move the time-line to the loaded frame:

<pre>
actions.add(new FSWaitForFrame(8, 1));
actions.add(new FSGotoFrame(8));
</pre>

<p>This method of waiting until a frame has been loaded is considered obsolete. Determining the number of frames loaded using the FramesLoaded property of the Flash player in combination with an FSIf action is now the preferred mechanism:</p>

<pre>
FSDoAction actions = new FSDoAction();

actions.add(new FSPush("_root"));
actions.add(new FSAction(FSAction.GetVariable));
actions.add(new FSPush(FSPush.FramesLoaded));
actions.add(new FSAction(FSAction.GetProperty));
actions.add(new FSPush(8));
actions.add(new FSAction(FSAction.Less));
actions.add(new FSIf(-29));

actions.add(new FSGotoFrame(8));
</pre>

<h1 class="datasheet">History</h1>

<p>The FSWaitForFrame class represents the ActionWaitForFrame action of the Macromedia Flash (SWF) File Format Specification. It was introduced in Flash 3.</p>
 */
public class FSWaitForFrame extends FSActionObject {

    private int frameNumber = 1;

    private int actionCount = 0;

    /**
     * Construct an FSWaitForFrame object, initalizing it with values decoded 
     * from an encoded object.
     * 
     * @param coder an FSCoder containing the binary data.
     */
    public FSWaitForFrame(FSCoder coder) {
        super(WaitForFrame);
        decode(coder);
    }

    /** Constructs an FSWaitForFrame object with the specified frame number and the number of actions that will be executed when the frame is loaded.

        @param aFrameNumber the number of the frame to wait for.
        @param anActionCount the number of actions to execute.
        */
    public FSWaitForFrame(int aFrameNumber, int anActionCount) {
        super(WaitForFrame);
        setFrameNumber(aFrameNumber);
        setActionCount(anActionCount);
    }

    /**
     * Constructs an FSWaitForFrame object by copying values from an existing 
     * object.
     *
     * @param obj an FSWaitForFrame object.
     */
    public FSWaitForFrame(FSWaitForFrame obj) {
        super(obj);
        frameNumber = obj.frameNumber;
        actionCount = obj.actionCount;
    }

    /** Gets the frame number.

        @return the number of the frame to wait for.
        */
    public int getFrameNumber() {
        return frameNumber;
    }

    /** Gets the number of actions that will be executed when the specified frame is loaded.

        @return the number of actions.
        */
    public int getActionCount() {
        return actionCount;
    }

    /** Sets the frame number.

        @param aNumber the number of the frame to wait for.
        */
    public void setFrameNumber(int aNumber) {
        frameNumber = aNumber;
    }

    /** Sets the number of actions to execute if the frame has been loaded. Unlike other actions it is the number of actions that are specified not the number of bytes in memory they occupy.

        @param aNumber the number of actions to execute.
        */
    public void setActionCount(int aNumber) {
        actionCount = aNumber;
    }

    public boolean equals(Object anObject) {
        boolean result = false;
        if (super.equals(anObject)) {
            FSWaitForFrame typedObject = (FSWaitForFrame) anObject;
            result = frameNumber == typedObject.frameNumber;
            result = result && actionCount == typedObject.actionCount;
        }
        return result;
    }

    public void appendDescription(StringBuffer buffer, int depth) {
        buffer.append(name());
        if (depth > 0) {
            buffer.append(": { ");
            Transform.append(buffer, "frameNumber", frameNumber);
            Transform.append(buffer, "actionCount", actionCount);
            buffer.append("}");
        }
    }

    public int length(FSCoder coder) {
        super.length(coder);
        length += 3;
        return length;
    }

    public void encode(FSCoder coder) {
        super.encode(coder);
        coder.writeWord(frameNumber, 2);
        coder.writeWord(actionCount, 1);
        coder.endObject(name());
    }

    public void decode(FSCoder coder) {
        super.decode(coder);
        frameNumber = coder.readWord(2, false);
        actionCount = coder.readWord(1, false);
        coder.endObject(name());
    }
}
