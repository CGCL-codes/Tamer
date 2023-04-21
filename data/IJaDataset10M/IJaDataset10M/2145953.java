package com.flagstone.transform;

/**
FSShowFrame is used to instruct the Flash Player to display a single frame in a movie or 
movie clip. 
 
<p>When a frame is displayed the Flash Player performs the following:</p>

<ul>
<li>The contents of the Flash Player's display list are drawn on the screen.</li>
<li>Any actions defined using a FSDoAction object are executed.</li>
</ul>

<table class="datasheet">

<tr><th align="left" colspan="2">Attributes</th></tr>

<tr><td><a name="FSShowFrame_0">type</a></td>
<td>Identifies the data structure when it is encoded. Read-only.</td>
</tr>

</table>

<p>Objects are placed in the display list using the FSPlaceObject and FSPlaceObject2 classes and removed using the FSRemoveObject and FSRemoveObject2 classes. An object which has been added to the display list will be displayed in each frame until it is explicitly removed. There is no need to repeatedly add it to the display list.</p>

<p>The scope of a frame is delineated by successive FSShowFrame objects. All the commands that affect change the state of the display list or define actions to be executed take effect when the Flash Player displays the frame. All the objects displayed in a frame must be defined before they can be displayed. The movie is displayed as it is downloaded so displaying objects that are defined later in a movie is not allowed.</p>

<pre>
FSMovie movie = new FSMovie();

// Frame 1 - starts from the beginning of the file.
...
movie.add(new FSShowFrame());

// Frame 2 - starts when the previous frame is displayed.

// All displayable objects are referenced using unique identifier.

int identifier = movie.newIdentifier();

// Define a shape to be displayed.

movie.add(new FSDefineShape(identifier, ......));

// Add the shape to the display list - on layer 1 at coordinates (400, 400)

movie.add(new FSPlaceObject(FSPlaceObject.New, identifier, 1, 400, 400));

// Add some actions

FSDoAction frameActions = new FSDoAction(); 

frameActions.add(anAction);
frameActions.add(anotherAction);

movie.add(frameActions);

// The shape is displayed and the actions executed when the FSShowFrame command is executed.

movie.add(new FSShowFrame());
</pre>
<h1 class="datasheet">History</h1>

<p>The FSShowFrame class represents the ShowFrame data structure from the Macromedia Flash (SWF) File Format Specification. It was introduced in Flash 1.</p>
 */
public class FSShowFrame extends FSMovieObject {

    private static final FSShowFrame instance = new FSShowFrame();

    /**
     * Returns a canonical FSShowFrame object.
     * 
     * @return an object that can safely be shared among objects.
     */
    public static FSShowFrame getInstance() {
        return instance;
    }

    /**
     * Construct an FSShowFrame object, initalizing it with values decoded from
     * an encoded object.
     * 
     * @param coder an FSCoder containing the binary data.
     */
    public FSShowFrame(FSCoder coder) {
        super(FSMovieObject.ShowFrame);
        decode(coder);
    }

    /** Constructs an FSShowFrame object. */
    public FSShowFrame() {
        super(FSMovieObject.ShowFrame);
    }

    /**
     * Constructs an FSShowFrame object by copying values from an existing 
     * object.
     *
     * @param obj an FSShowFrame object.
     */
    public FSShowFrame(FSShowFrame obj) {
        super(obj);
    }

    public void appendDescription(StringBuffer buffer, int depth) {
        buffer.append(name());
    }

    public int length(FSCoder coder) {
        super.length(coder);
        return length;
    }

    public void encode(FSCoder coder) {
        coder.context[FSCoder.Empty] = 1;
        super.encode(coder);
        coder.context[FSCoder.Empty] = 0;
        coder.endObject(name());
    }

    public void decode(FSCoder coder) {
        super.decode(coder);
        coder.endObject(name());
    }
}
