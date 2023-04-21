package com.flanderra;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class TagDefineShape3 implements IOStruct {

    public Map read(BitInputStream bis) throws IOException {
        Map result = new HashMap();
        result.put("shapeId", BitUtils._parseUI16(new byte[] { bis.read(), bis.read() }));
        Map bounds = BasicTypesUtils.rect(bis);
        result.put("shapeBounds", bounds);
        Map shape = new HashMap();
        List shapeRecords = new ArrayList();
        shape.putAll(BasicTypesUtils.fillstyles2(bis));
        shape.putAll(BasicTypesUtils.linestyles2(bis));
        Long numFillBits = BitUtils._parseUBArray(bis.readNext(4));
        Long numLineBits = BitUtils._parseUBArray(bis.readNext(4));
        boolean endRecordReached = false;
        while (!endRecordReached) {
            Map shapeRecord = new HashMap();
            Long flag0 = BitUtils._parseUBArray(bis.readNext(1));
            Long flag1 = BitUtils._parseUBArray(bis.readNext(1));
            Long flag2 = BitUtils._parseUBArray(bis.readNext(1));
            Long flag3 = BitUtils._parseUBArray(bis.readNext(1));
            Long flag4 = BitUtils._parseUBArray(bis.readNext(1));
            Long flag5 = BitUtils._parseUBArray(bis.readNext(1));
            if (flag0 == 0 && (flag1 + flag2 + flag3 + flag4 + flag5 == 0)) {
                shapeRecord.put("type", "endrecord");
                bis.align();
                endRecordReached = true;
            } else if (flag0 == 0 && (flag1 + flag2 + flag3 + flag4 + flag5 != 0)) {
                shapeRecord.put("type", "stylechange");
                Long stateNewStyles = flag1;
                Long stateLineStyle = flag2;
                Long stateFillStyle1 = flag3;
                Long stateFillStyle0 = flag4;
                Long stateMoveTo = flag5;
                shapeRecord.put("stateNewStyles", stateNewStyles);
                shapeRecord.put("stateLineStyle", stateLineStyle);
                shapeRecord.put("stateFillStyle1", stateFillStyle1);
                shapeRecord.put("stateFillStyle0", stateFillStyle0);
                shapeRecord.put("stateMoveTo", stateMoveTo);
                if (stateMoveTo == 1) {
                    Long moveToBits = BitUtils._parseUBArray(bis.readNext(5));
                    Long moveDeltaX = BitUtils._parseSBArray(bis.readNext(moveToBits.intValue()));
                    Long moveDeltaY = BitUtils._parseSBArray(bis.readNext(moveToBits.intValue()));
                    shapeRecord.put("moveToBits", moveToBits);
                    shapeRecord.put("moveDeltaX", moveDeltaX);
                    shapeRecord.put("moveDeltaY", moveDeltaY);
                }
                if (stateFillStyle0 == 1) {
                    Long fillStyle0 = BitUtils._parseUBArray(bis.readNext(numFillBits.intValue()));
                    shapeRecord.put("fillStyle0", fillStyle0);
                }
                if (stateFillStyle1 == 1) {
                    Long fillStyle1 = BitUtils._parseUBArray(bis.readNext(numFillBits.intValue()));
                    shapeRecord.put("fillStyle1", fillStyle1);
                }
                if (stateLineStyle == 1) {
                    Long lineStyle = BitUtils._parseUBArray(bis.readNext(numLineBits.intValue()));
                    shapeRecord.put("lineStyle", lineStyle);
                }
                if (stateNewStyles == 1) {
                }
            } else if (flag0 == 1 && flag1 == 0) {
                shapeRecord.put("type", "curvededge");
                Long numBits = 0 | flag2 << 3 | flag3 << 2 | flag4 << 1 | flag5;
                shapeRecord.put("numBits", numBits);
                Long controlDeltaX = BitUtils._parseSBArray(bis.readNext(numBits.intValue() + 2));
                ;
                Long controlDeltaY = BitUtils._parseSBArray(bis.readNext(numBits.intValue() + 2));
                ;
                Long anchorDeltaX = BitUtils._parseSBArray(bis.readNext(numBits.intValue() + 2));
                ;
                Long anchorDeltaY = BitUtils._parseSBArray(bis.readNext(numBits.intValue() + 2));
                ;
                shapeRecord.put("controlDeltaX", controlDeltaX);
                shapeRecord.put("controlDeltaY", controlDeltaY);
                shapeRecord.put("anchorDeltaX", anchorDeltaX);
                shapeRecord.put("anchorDeltaY", anchorDeltaY);
            } else if (flag0 == 1 && flag1 == 1) {
                shapeRecord.put("type", "straightedge");
                Long numBits = 0 | flag2 << 3 | flag3 << 2 | flag4 << 1 | flag5;
                shapeRecord.put("numBits", numBits);
                Long generalFlag = BitUtils._parseUBArray(bis.readNext(1));
                Long deltaX = 0L;
                Long deltaY = 0L;
                shapeRecord.put("generalFlag", generalFlag);
                if (generalFlag == 1) {
                    deltaX = BitUtils._parseSBArray(bis.readNext(numBits.intValue() + 2));
                    deltaY = BitUtils._parseSBArray(bis.readNext(numBits.intValue() + 2));
                } else {
                    Long verticalFlag = BitUtils._parseUBArray(bis.readNext(1));
                    if (verticalFlag == 1) {
                        deltaY = BitUtils._parseSBArray(bis.readNext(numBits.intValue() + 2));
                    } else {
                        deltaX = BitUtils._parseSBArray(bis.readNext(numBits.intValue() + 2));
                    }
                    shapeRecord.put("verticalFlag", verticalFlag);
                }
                shapeRecord.put("deltaX", deltaX);
                shapeRecord.put("deltaY", deltaY);
            } else {
            }
            shapeRecords.add(shapeRecord);
        }
        shape.put("numFillBits", numFillBits);
        shape.put("numLineBits", numLineBits);
        shape.put("shapeRecords", shapeRecords);
        result.put("shape", shape);
        return result;
    }

    public Bits write(Map data) throws IOException {
        Bits result = null;
        Long shapeId = TypeUtils.toLong(data.get("shapeId"));
        Bits shapeIdB = BitUtils._bitsUI16(shapeId);
        Map bounds = (Map) data.get("shapeBounds");
        Bits boundsB = BitUtils._bytesToBits(BasicTypesUtils.rect(bounds).getData());
        Map shape = (Map) data.get("shape");
        Long numFillBits = null;
        if (shape.containsKey("numFillBits")) {
            numFillBits = TypeUtils.toLong(shape.get("numFillBits"));
        } else {
            if (shape.containsKey("fillStyles")) {
                numFillBits = (long) BitUtils._calculateMaxBitCountUI(new long[] { ((List) shape.get("fillStyles")).size() });
            } else {
                numFillBits = 0L;
            }
        }
        Long numLineBits = null;
        if (shape.containsKey("numLineBits")) {
            numLineBits = TypeUtils.toLong(shape.get("numLineBits"));
        } else {
            if (shape.containsKey("lineStyles")) {
                numLineBits = (long) BitUtils._calculateMaxBitCountUI(new long[] { ((List) shape.get("lineStyles")).size() });
            } else {
                numLineBits = 0L;
            }
        }
        Bits numFillBitsB = BitUtils._bitsUBArray(numFillBits, 4);
        Bits numLineBitsB = BitUtils._bitsUBArray(numLineBits, 4);
        Bits fillStylesB = null;
        fillStylesB = BasicTypesUtils.fillstyles2(shape);
        Bits lineStylesB = BasicTypesUtils.linestyles2(shape);
        List shapeRecords = (List) shape.get("shapeRecords");
        Bits shapeRecordsB = new Bits(new byte[] {}, 0);
        for (Iterator iterator = shapeRecords.iterator(); iterator.hasNext(); ) {
            Bits shapeRecordB = null;
            Map shapeRecord = (Map) iterator.next();
            String recordType = (String) shapeRecord.get("type");
            if (recordType.equals("endrecord")) {
                shapeRecordB = BitUtils._bitsUBArray(0, 6);
            } else if (recordType.equals("stylechange")) {
                Long stateNewStyles = TypeUtils.toLong(shapeRecord.get("stateNewStyles"));
                Long stateLineStyle = TypeUtils.toLong(shapeRecord.get("stateLineStyle"));
                Long stateFillStyle1 = TypeUtils.toLong(shapeRecord.get("stateFillStyle1"));
                Long stateFillStyle0 = TypeUtils.toLong(shapeRecord.get("stateFillStyle0"));
                Long stateMoveTo = TypeUtils.toLong(shapeRecord.get("stateMoveTo"));
                shapeRecordB = BitUtils._bitsUBArray(0 | stateNewStyles << 4 | stateLineStyle << 3 | stateFillStyle1 << 2 | stateFillStyle0 << 1 | stateMoveTo, 6);
                if (stateMoveTo == 1) {
                    Long moveToBits = TypeUtils.toLong(shapeRecord.get("moveToBits"));
                    Long moveDeltaX = TypeUtils.toLong(shapeRecord.get("moveDeltaX"));
                    Long moveDeltaY = TypeUtils.toLong(shapeRecord.get("moveDeltaY"));
                    shapeRecordB = BitUtils._concat(new Bits[] { shapeRecordB, BitUtils._bitsUBArray(moveToBits, 5), BitUtils._bitsSBArray(moveDeltaX, moveToBits.intValue()), BitUtils._bitsSBArray(moveDeltaY, moveToBits.intValue()) });
                }
                if (stateFillStyle0 == 1) {
                    Long fillStyle0 = TypeUtils.toLong(shapeRecord.get("fillStyle0"));
                    shapeRecordB = BitUtils._concat(new Bits[] { shapeRecordB, BitUtils._bitsUBArray(fillStyle0, numFillBits.intValue()) });
                }
                if (stateFillStyle1 == 1) {
                    Long fillStyle1 = TypeUtils.toLong(shapeRecord.get("fillStyle1"));
                    shapeRecordB = BitUtils._concat(new Bits[] { shapeRecordB, BitUtils._bitsUBArray(fillStyle1, numFillBits.intValue()) });
                }
                if (stateLineStyle == 1) {
                    Long lineStyle = TypeUtils.toLong(shapeRecord.get("lineStyle"));
                    shapeRecordB = BitUtils._concat(new Bits[] { shapeRecordB, BitUtils._bitsUBArray(lineStyle, numLineBits.intValue()) });
                }
                if (stateNewStyles == 1) {
                }
            } else if (recordType.equals("curvededge")) {
                Long controlDeltaX = TypeUtils.toLong(shapeRecord.get("controlDeltaX"));
                Long controlDeltaY = TypeUtils.toLong(shapeRecord.get("controlDeltaY"));
                Long anchorDeltaX = TypeUtils.toLong(shapeRecord.get("anchorDeltaX"));
                Long anchorDeltaY = TypeUtils.toLong(shapeRecord.get("anchorDeltaY"));
                Long numBits = 0L;
                if (shapeRecord.containsKey("numBits")) {
                    numBits = TypeUtils.toLong(shapeRecord.get("numBits"));
                } else {
                    numBits = new Long(BitUtils._calculateMaxBitCountSI(new long[] { controlDeltaX.longValue(), controlDeltaY.longValue(), anchorDeltaX.longValue(), anchorDeltaY.longValue() })) - 2;
                }
                Bits numBitsB = BitUtils._bitsUBArray(numBits, 4);
                shapeRecordB = BitUtils._concat(new Bits[] { BitUtils._bitsUBArray(2, 2), numBitsB, BitUtils._bitsSBArray(controlDeltaX, numBits.intValue() + 2), BitUtils._bitsSBArray(controlDeltaY, numBits.intValue() + 2), BitUtils._bitsSBArray(anchorDeltaX, numBits.intValue() + 2), BitUtils._bitsSBArray(anchorDeltaY, numBits.intValue() + 2) });
            } else if (recordType.equals("straightedge")) {
                Long deltaX = TypeUtils.toLong(shapeRecord.get("deltaX"));
                Long deltaY = TypeUtils.toLong(shapeRecord.get("deltaY"));
                Long numBits = 0L;
                if (shapeRecord.containsKey("numBits")) {
                    numBits = TypeUtils.toLong(shapeRecord.get("numBits"));
                } else {
                    numBits = new Long(BitUtils._calculateMaxBitCountSI(new long[] { deltaX.longValue(), deltaY.longValue() })) - 2;
                }
                Bits numBitsB = BitUtils._bitsUBArray(numBits, 4);
                Long generalFlag = 1L;
                if (shapeRecord.containsKey("generalFlag")) {
                    generalFlag = TypeUtils.toLong(shapeRecord.get("generalFlag"));
                } else {
                    generalFlag = (deltaX != 0L && deltaY != 0L) ? 1L : 0L;
                }
                Bits generalFlagB = BitUtils._bitsUBArray(generalFlag, 1);
                shapeRecordB = BitUtils._concat(new Bits[] { BitUtils._bitsUBArray(3, 2), numBitsB, generalFlagB });
                if (generalFlag == 1) {
                    shapeRecordB = BitUtils._concat(new Bits[] { shapeRecordB, BitUtils._bitsSBArray(deltaX, numBits.intValue() + 2), BitUtils._bitsSBArray(deltaY, numBits.intValue() + 2) });
                } else {
                    Long verticalFlag = 0L;
                    if (shapeRecord.containsKey("verticalFlag")) {
                        verticalFlag = TypeUtils.toLong(shapeRecord.get("verticalFlag"));
                    } else {
                        verticalFlag = (deltaX == 0) ? 1L : 0L;
                    }
                    Bits verticalFlagB = BitUtils._bitsUBArray(verticalFlag, 1);
                    shapeRecordB = BitUtils._concat(new Bits[] { shapeRecordB, verticalFlagB, (verticalFlag == 1) ? BitUtils._bitsSBArray(deltaY, numBits.intValue() + 2) : BitUtils._bitsSBArray(deltaX, numBits.intValue() + 2) });
                }
            }
            shapeRecordsB = BitUtils._concat(shapeRecordsB, shapeRecordB);
        }
        shapeRecordsB = BitUtils._bytesToBits(shapeRecordsB.getData());
        result = BitUtils._concat(new Bits[] { shapeIdB, boundsB, fillStylesB, lineStylesB, numFillBitsB, numLineBitsB, shapeRecordsB });
        return result;
    }
}
