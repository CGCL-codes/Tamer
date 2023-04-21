package com.flagstone.transform;

/**
FSRemoveObject removes an object from the Flash Player's Display List. 

<p>An object placed on the display list is displayed in every frame of a movie until it is explicitly removed. Objects must also be removed if its location or appearance is changed using either the FSPlaceObject or FSPlaceObject2 classes.</p>

<table class="datasheet">

<tr><th align="left" colspan="2">Attributes</th></tr>

<tr><td><a name="FSRemoveobject_0">type</a></td>
<td>Identifies the data structure when it is encoded. Read-only.</td>
</tr>

<tr>
<td><a name="FSRemoveobject_1">identifier</a></td>
<td>The unique identifier, in the range 1..65535, of the object.</td>
</tr>

<tr>
<td><a name="FSRemoveobject_2">layer</a></td>
<td>The layer at which the object is placed in the Display List.</td>
</tr>
</table>

<p>Although only one object can be placed on any layer in the display list both the object's unique identifier and the layer number must be specified. The FSRemoveObject class is superseded in Flash 3 by the FSRemoveObject2 class which lifts this requirement allowing an object to be referenced by the layer number it occupies in the display list.</p>

<h1 class="datasheet">Examples</h1>

<p>1. Remove an object.<br/>
To remove an object from the display list the object's identifier and the layer number using when the object was placed is used.</p>

<pre>
// Place a shape to the display list for one frame.
movie.add(new FSPlaceObject(shape.getIdentifier(), 1, 400, 400));
movie.add(new FSShowFrame());

// now remove it.
movie.add(new FSRemoveObject(shape.getIdentifier(), 1));
movie.add(new FSShowFrame());
</pre>

<p>2. Move an object.<br/>
To move an object it first must be removed from the display list and repositioned at its new location. Adding the object, with a new location, on the same layer, although only one object can be displayed on a given layer, will not work. The object will be displayed twice.</p>

<pre>
// Add the shape to the display list.
movie.add(new FSPlaceObject(shape.getIdentifier(), 1, 400, 400));
movie.add(new FSShowFrame());

// Move shape to a new location, removing the original so it does not get displayed twice.
movie.add(new FSRemoveObject(shape.getIdentifier(), 1));
movie.add(new FSPlaceObject(shape.getIdentifier(), 1, 250, 300));
movie.add(new FSShowFrame());
</pre>


<h1 class="datasheet">History</h1>

<p>The FSRemoveObject class represents the RemoveObject tag from the Macromedia Flash (SWF) File Format Specification. It was introduced in Flash 1 and is superseded by the RemoveObject2 tag which was added in Flash 3.</p>
 */
public class FSRemoveObject extends FSMovieObject {

    private int identifier = 0;

    private int layer = 0;

    /**
     * Construct an FSRemoveObject object, initalizing it with values decoded 
     * from an encoded object.
     * 
     * @param coder an FSCoder containing the binary data.
     */
    public FSRemoveObject(FSCoder coder) {
        super(FSMovieObject.RemoveObject);
        decode(coder);
    }

    /**  Constructs an FSRemoveObject object that will remove an object with the specified identifier from the given layer in the display list.

        @param anIdentifier the unique identifier for the object currently on the display list.
        @param layer the layer in the display list where the object is being displayed.
        */
    public FSRemoveObject(int anIdentifier, int layer) {
        super(FSMovieObject.RemoveObject);
        setIdentifier(anIdentifier);
        setLayer(layer);
    }

    /**
     * Constructs an FSRemoveObject object by copying values from an existing 
     * object.
     *
     * @param obj an FSRemoveObject object.
     */
    public FSRemoveObject(FSRemoveObject obj) {
        super(obj);
        identifier = obj.identifier;
        layer = obj.layer;
    }

    /** Gets the identifier of the object to be removed from the display list.

        @return the identifier of the object to be removed.
        */
    public int getIdentifier() {
        return identifier;
    }

    /** Gets the layer in the display list where the object will be displayed.

        @return the layer number where the object to be removed is displayed.
        */
    public int getLayer() {
        return layer;
    }

    /** Sets the identifier of the object to be removed.

        @param anIdentifier the unique identifier for the object currently on the display list.
        */
    public void setIdentifier(int anIdentifier) {
        identifier = anIdentifier;
    }

    /** Sets the layer in the display list where the object will be displayed.

        @param aLayer the layer in the display list where the object is being displayed.
        */
    public void setLayer(int aLayer) {
        layer = aLayer;
    }

    public boolean equals(Object anObject) {
        boolean result = false;
        if (super.equals(anObject)) {
            FSRemoveObject typedObject = (FSRemoveObject) anObject;
            result = identifier == typedObject.identifier;
            result = result && layer == typedObject.layer;
        }
        return result;
    }

    public void appendDescription(StringBuffer buffer, int depth) {
        buffer.append(name());
        if (depth > 0) {
            buffer.append(": { ");
            Transform.append(buffer, "identifier", identifier);
            Transform.append(buffer, "layer", layer);
            buffer.append("}");
        }
    }

    public int length(FSCoder coder) {
        super.length(coder);
        length += 4;
        return length;
    }

    public void encode(FSCoder coder) {
        super.encode(coder);
        coder.writeWord(identifier, 2);
        coder.writeWord(layer, 2);
        coder.endObject(name());
    }

    public void decode(FSCoder coder) {
        super.decode(coder);
        identifier = coder.readWord(2, false);
        layer = coder.readWord(2, false);
        coder.endObject(name());
    }
}
