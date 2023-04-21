package com.trapezium.chisel.mutators;

import com.trapezium.chisel.*;
import com.trapezium.vrml.node.space.SpaceStructure;
import com.trapezium.vrml.node.space.SpaceEntitySet;
import com.trapezium.vrml.node.space.SpacePrimitive;
import com.trapezium.parse.TokenTypes;

public class IFS_Smasher extends Origami {

    boolean smashOnX;

    boolean smashOnY;

    boolean smashOnZ;

    public IFS_Smasher() {
        super();
        dx = 1;
        dy = 1;
        dz = 1;
        smashOnX = false;
        smashOnY = false;
        smashOnZ = true;
    }

    boolean smashedOnX;

    boolean smashedOnY;

    boolean smashedOnZ;

    int callCount;

    /** Replace the entire set of coordinates once for each smash plane. 
     *  NOTE:  this increases the number of coordinates, so if there are
     *  any other fields that are supposed to correspond one-for-one with
     *  the coordinates, these must get repeated as well (not implemented)
     *
     *  @param tp TokenPrinter to print to
     *  @param ss the SpaceStructure containing the locatinos
     *  @param startTokenOffset first token in range to replace
     *  @param lastTokenOffset last token in range to replace 
     */
    public void replaceCoord(TokenPrinter tp, SpaceStructure ss, int startTokenOffset, int endTokenOffset) {
        callCount = 0;
        if (smashOnX) callCount++;
        if (smashOnY) callCount++;
        if (smashOnZ) callCount++;
        smashedOnX = smashOnX;
        smashedOnY = smashOnY;
        smashedOnZ = smashOnZ;
        for (int i = 0; i < callCount; i++) {
            super.replaceCoord(tp, ss, startTokenOffset, endTokenOffset, (i == 0), (i == (callCount - 1)));
            if (smashedOnX) {
                smashedOnX = false;
            } else if (smashedOnY) {
                smashedOnY = false;
            } else if (smashedOnZ) {
                smashedOnZ = false;
            }
        }
    }

    public void printCoords(TokenPrinter tp, SpaceEntitySet mc, SpacePrimitive sp) {
        if (smashedOnX) {
            tp.print((float) mc.getX());
            tp.print((float) sp.getY());
            tp.print((float) sp.getZ());
        } else {
            tp.print((float) sp.getX());
            if (smashedOnY) {
                tp.print((float) mc.getY());
                tp.print((float) sp.getZ());
            } else {
                tp.print((float) sp.getY());
                if (smashedOnZ) {
                    tp.print((float) mc.getZ());
                } else {
                    tp.print((float) sp.getZ());
                }
            }
        }
    }

    /** Replace the coordIndex values.  
     *  NOTE: this may create multiple sets of faces, any other fields which 
     *  correspond one-per-face must also be generated multiple times.
     */
    public void replaceCoordIndex(TokenPrinter tp, SpaceStructure ss, int startTokenOffset, int endTokenOffset) {
        for (int i = 0; i < callCount; i++) {
            replaceCoordIndex(i * numberVertices, tp, startTokenOffset, endTokenOffset, (i == 0), (i == (callCount - 1)));
        }
    }

    /** Replace the colorIndex values.
     *  NOTE:  the colorIndex may be one per face, so is generated multiple
     *  times.
     */
    public void replaceColorIndex(TokenPrinter tp, SpaceStructure ss, int startTokenOffset, int endTokenOffset) {
        for (int i = 0; i < callCount; i++) {
            replaceColors(tp, startTokenOffset, endTokenOffset, (i == 0), (i == (callCount - 1)));
        }
    }

    public void replaceColor(TokenPrinter tp, SpaceStructure ss, int startTokenOffset, int endTokenOffset) {
        for (int i = 0; i < callCount; i++) {
            replaceColors(tp, startTokenOffset, endTokenOffset, (i == 0), (i == (callCount - 1)));
        }
    }

    public void replaceColors(TokenPrinter tp, int startTokenOffset, int endTokenOffset, boolean printStart, boolean printEnd) {
        int scanner = startTokenOffset;
        dataSource.setState(scanner);
        while (true) {
            int type = dataSource.getType(scanner);
            if (printStart) {
                tp.print(dataSource, scanner, type);
            }
            if (type == TokenTypes.LeftBracket) {
                scanner = dataSource.getNextToken();
                break;
            }
            scanner = dataSource.getNextToken();
        }
        while (true) {
            int type = dataSource.getType(scanner);
            if (type == TokenTypes.RightBracket) {
                break;
            }
            tp.print(dataSource, scanner, type);
            scanner = dataSource.getNextToken();
        }
        while (true) {
            if (printEnd) {
                tp.print(dataSource, scanner);
            }
            if (scanner == endTokenOffset) {
                break;
            }
            scanner = dataSource.getNextToken();
        }
    }

    int firstNumberOffset;

    void replaceCoordIndex(int offset, TokenPrinter tp, int startTokenOffset, int endTokenOffset, boolean printStart, boolean printEnd) {
        int scanner;
        if (printStart) {
            scanner = startTokenOffset;
            dataSource.setState(scanner);
            while (true) {
                int type = dataSource.getType(scanner);
                tp.print(dataSource, scanner, type);
                if (type == TokenTypes.LeftBracket) {
                    break;
                }
                scanner = dataSource.getNextToken();
            }
            scanner = firstNumberOffset = dataSource.getNextToken();
        } else {
            scanner = firstNumberOffset;
            dataSource.setState(scanner);
        }
        while (true) {
            int type = dataSource.getType(scanner);
            if (type == TokenTypes.RightBracket) {
                break;
            } else if (type == TokenTypes.NumberToken) {
                int value = dataSource.getIntValue(scanner);
                if (value != -1) {
                    value += offset;
                }
                tp.print(value);
            } else {
                tp.print(dataSource, scanner, type);
            }
            scanner = dataSource.getNextToken();
        }
        if (printEnd) {
            tp.print(dataSource, scanner);
        }
    }

    /** Get the class for an option */
    public Class getOptionClass(int offset) {
        return (Boolean.TYPE);
    }

    public String getOptionLabel(int offset) {
        switch(offset) {
            case DX:
                return ("smash on X plane");
            case DY:
                return ("smash on Y plane");
            case DZ:
                return ("smash on Z plane");
        }
        return null;
    }

    public Object getOptionValue(int offset) {
        switch(offset) {
            case DX:
                return (booleanToOptionValue(smashOnX));
            case DY:
                return (booleanToOptionValue(smashOnY));
            case DZ:
                return (booleanToOptionValue(smashOnZ));
        }
        return "";
    }

    public void setOptionValue(int offset, Object value) {
        switch(offset) {
            case DX:
                smashOnX = optionValueToBoolean(value);
                break;
            case DY:
                smashOnY = optionValueToBoolean(value);
                break;
            case DZ:
                smashOnZ = optionValueToBoolean(value);
                break;
        }
    }
}
