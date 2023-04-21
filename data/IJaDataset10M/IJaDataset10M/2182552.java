package com.flagstone.transform;

/**
FSStartSound instructs the player to start or stop playing a sound defined using the 
 FSDefineSound class.
 
<table class="datasheet">

<tr><th align="left" colspan="2">Attributes</th></tr>

<tr><td><a name="FSStartSound_0">type</a></td>
<td>Identifies the data structure when it is encoded. Read-only.</td>
</tr>

<tr><td><a name="FSStartSound_1">sound</a></td>
<td>An FSSound object that identifies the sound and controls how it is played - how the sound fades in or out, whether it is repeated along with an envelope that controls the sound levels as it is played.</td>
</tr>

</table>

<h1 class="datasheet">History</h1>

<p>The FSStartSound class represents the StartSound tag from the Macromedia Flash (SWF) File Format Specification. It was introduced in Flash 1. In the Macromedia Flash (SWF) File Format Specification the StartSound tag contains an identifier and an FSSoundInfo structure. The Transform FSSound object simply combines both to simplify the design of other sound classes. The information encoded is identical.</p>
 */
public class FSStartSound extends FSMovieObject {

    private FSSound sound = null;

    /**
     * Construct an FSStartSound object, initalizing it with values decoded from
     * an encoded object.
     * 
     * @param coder an FSCoder containing the binary data.
     */
    public FSStartSound(FSCoder coder) {
        super(FSMovieObject.StartSound);
        decode(coder);
    }

    /** Constructs an FSStartSound object with an FSSound object that identifies the sound and controls how it is played.

        @param aSound the FSSound object.
        */
    public FSStartSound(FSSound aSound) {
        super(FSMovieObject.StartSound);
        setSound(aSound);
    }

    /**
     * Constructs an FSStartSound object by copying values from an existing 
     * object.
     *
     * @param obj an FSStartSound object.
     */
    public FSStartSound(FSStartSound obj) {
        super(obj);
        sound = new FSSound(obj.sound);
    }

    /**  Gets the FSSound object describing how the sound will be played.

        @return the FSSound object.
        */
    public FSSound getSound() {
        return sound;
    }

    /**  Sets the FSSound object that describes how the sound will be played.

        @param aSound the FSSound object that controls how the sound is played.
        */
    public void setSound(FSSound aSound) {
        sound = aSound;
    }

    public Object clone() {
        FSStartSound anObject = (FSStartSound) super.clone();
        anObject.sound = (sound != null) ? (FSSound) sound.clone() : null;
        return anObject;
    }

    public boolean equals(Object anObject) {
        boolean result = false;
        if (super.equals(anObject)) {
            FSStartSound typedObject = (FSStartSound) anObject;
            if (sound != null) result = sound.equals(typedObject.sound); else result = sound == typedObject.sound;
        }
        return result;
    }

    public void appendDescription(StringBuffer buffer, int depth) {
        buffer.append(name());
        if (depth > 0) {
            buffer.append(": { ");
            Transform.append(buffer, "sound", sound, depth);
            buffer.append("}");
        }
    }

    public int length(FSCoder coder) {
        super.length(coder);
        length += sound.length(coder);
        return length;
    }

    public void encode(FSCoder coder) {
        super.encode(coder);
        sound.encode(coder);
        coder.endObject(name());
    }

    public void decode(FSCoder coder) {
        super.decode(coder);
        sound = new FSSound(coder);
        coder.endObject(name());
    }
}
