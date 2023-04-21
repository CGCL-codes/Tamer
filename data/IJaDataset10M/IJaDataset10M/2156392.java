package swf9.tags;

import swf9.util.ByteUtils;
import swf9.tags.PlaceObject;
import swf9.types.*;

public class PlaceObject2 extends PlaceObject {

    public int PlaceFlagHasClipActions;

    public int PlaceFlagHasClipDepth;

    public int PlaceFlagHasName;

    public int PlaceFlagHasRatio;

    public int PlaceFlagHasColorTransform;

    public int PlaceFlagHasMatrix;

    public int PlaceFlagHasCharacter;

    public int PlaceFlagMove;

    public int Ratio;

    public String Name;

    public int ClipDepth;

    public CLIPACTIONS ClipActions;

    public static int HasClipActions = 0x80;

    public static int HasClipDepth = 0x40;

    public static int HasName = 0x20;

    public static int HasRatio = 0x10;

    public static int HasColorTransform = 0x08;

    public static int HasMatrix = 0x04;

    public static int HasCharacter = 0x02;

    public static int FlagMove = 0x01;

    public PlaceObject2() {
        type = PlaceObject2;
        length = 5;
        PlaceFlagHasClipActions = 0;
        PlaceFlagHasClipDepth = 0;
        PlaceFlagHasName = 0;
        PlaceFlagHasRatio = 0;
        PlaceFlagHasColorTransform = 0;
        PlaceFlagHasMatrix = 0;
        PlaceFlagHasCharacter = 0;
        PlaceFlagMove = 0;
    }

    public PlaceObject2(byte[] swf, int offset) {
        type = PlaceObject2;
        getTagLength(swf, offset);
        Depth = swf[offset + 2];
        int index = offset + 4;
        if ((swf[offset] & FlagMove) == FlagMove) PlaceFlagMove = 1;
        if ((swf[offset] & HasCharacter) == HasCharacter) {
            PlaceFlagHasCharacter = 1;
            CharacterID = swf[index];
            index += 2;
        }
        if ((swf[offset] & HasMatrix) == HasMatrix) {
            PlaceFlagHasMatrix = 1;
            Matrix = new MATRIX(swf, index);
            index += Matrix.length;
        }
        if ((swf[offset] & HasColorTransform) == HasColorTransform) {
            PlaceFlagHasColorTransform = 1;
            ColorTransform = new CXFORMWITHALPHA(swf, index);
            index += ColorTransform.length;
        }
        if ((swf[offset] & HasRatio) == HasRatio) {
            PlaceFlagHasRatio = 1;
            Ratio = 1;
            index += 2;
        }
        if ((swf[offset] & HasName) == HasName) {
            PlaceFlagHasName = 1;
            Name = "--";
            index += Name.length();
        }
        if ((swf[offset] & HasClipDepth) == HasClipDepth) {
            PlaceFlagHasClipDepth = 1;
            ClipDepth = 1;
            index += 2;
        }
        if ((swf[offset] & HasClipActions) == HasClipActions) {
            PlaceFlagHasClipActions = 1;
            ClipActions = new CLIPACTIONS(swf, index);
            index += ClipActions.length;
        }
    }

    public PlaceObject2(int depth, int flags, int charID, MATRIX matrix, CXFORMWITHALPHA cxform, int ratio, String name, int clipDepth, CLIPACTIONS clipActions) {
        type = PlaceObject2;
        length = 5;
        Depth = depth;
        if ((flags & FlagMove) == FlagMove) PlaceFlagMove = 1;
        if ((flags & HasCharacter) == HasCharacter) {
            PlaceFlagHasCharacter = 1;
            CharacterID = charID;
            length += 2;
        }
        if ((flags & HasMatrix) == HasMatrix) {
            PlaceFlagHasMatrix = 1;
            Matrix = matrix;
            length += Matrix.length;
        }
        if ((flags & HasColorTransform) == HasColorTransform) {
            PlaceFlagHasColorTransform = 1;
            ColorTransform = cxform;
            length += ColorTransform.length;
        }
        if ((flags & HasRatio) == HasRatio) {
            PlaceFlagHasRatio = 1;
            Ratio = ratio;
            length += 2;
        }
        if ((flags & HasName) == HasName) {
            PlaceFlagHasName = 1;
            Name = name;
            length += Name.length();
        }
        if ((flags & HasClipDepth) == HasClipDepth) {
            PlaceFlagHasClipDepth = 1;
            ClipDepth = clipDepth;
            length += 2;
        }
        if ((flags & HasClipActions) == HasClipActions) {
            PlaceFlagHasClipActions = 1;
            ClipActions = clipActions;
            length += ClipActions.length;
        }
    }

    public byte[] toByteArray() {
        byte[] bytes = new byte[length];
        bytes = writeHeader(bytes, type, length - 2);
        bytes[2] = (byte) (PlaceFlagHasClipActions << 7 | PlaceFlagHasClipDepth << 6 | PlaceFlagHasName << 5 | PlaceFlagHasRatio << 4 | PlaceFlagHasColorTransform << 3 | PlaceFlagHasMatrix << 2 | PlaceFlagHasCharacter << 1 | PlaceFlagMove);
        System.arraycopy(ByteUtils.intToByte(Depth, 2, true), 0, bytes, 3, 2);
        int offset = 5;
        if (PlaceFlagHasCharacter == 1) {
            System.arraycopy(ByteUtils.intToByte(CharacterID, 2, true), 0, bytes, offset, 2);
            offset += 2;
        }
        if (PlaceFlagHasMatrix == 1) {
            System.arraycopy(Matrix.toByteArray(), 0, bytes, offset, Matrix.length);
            offset += Matrix.length;
        }
        if (PlaceFlagHasColorTransform == 1) {
            System.arraycopy(ColorTransform.toByteArray(), 0, bytes, offset, ColorTransform.length);
            offset += ColorTransform.length;
        }
        if (PlaceFlagHasRatio == 1) {
            System.arraycopy(ByteUtils.intToByte(Ratio, 2, true), 0, bytes, offset, 2);
            offset += 2;
        }
        if (PlaceFlagHasName == 1) {
            System.arraycopy(Name.getBytes(), 0, bytes, offset, Name.length());
            offset += Name.length();
        }
        if (PlaceFlagHasClipDepth == 1) {
            System.arraycopy(ByteUtils.intToByte(ClipDepth, 2, true), 0, bytes, offset, 2);
            offset += 2;
        }
        if (PlaceFlagHasClipActions == 1) {
            System.arraycopy(ClipActions.toByteArray(), 0, bytes, offset, ClipActions.length);
            offset += ClipActions.length;
        }
        return bytes;
    }
}
