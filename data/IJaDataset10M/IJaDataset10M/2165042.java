package com.ynhenc.droute.map.link;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.sql.*;
import oracle.sql.*;
import com.ynhenc.comm.db.QueryResult;
import com.ynhenc.comm.db.SQL;
import com.ynhenc.droute.*;
import com.ynhenc.kml.KmlHandlerLinkShape;

public class LinkShape extends ComLibRoute {

    public StringBuffer toKmlCoords() {
        double coords[] = this.getCoords();
        StringBuffer bfr = new StringBuffer();
        for (int i = 0, iLen = coords.length / 2; i < iLen; i++) {
            bfr.append(coords[i * 2] + "," + coords[i * 2 + 1] + ",0");
            if (i < iLen - 1) {
                bfr.append(",");
            }
        }
        return bfr;
    }

    public double[] getCoords() {
        if (this.coords == null) {
            try {
                this.coords = this.loadShape();
            } catch (Exception e) {
                this.debug(e, true);
            }
        }
        return coords;
    }

    public long getLinkId() {
        return linkId;
    }

    private double[] loadShape() throws Exception {
        if (true) {
            return this.loadShapeFromWkb();
        } else {
            return this.loadShapeFromSdoGeometry();
        }
    }

    private double[] loadShapeFromWkb() throws Exception {
        boolean localDebug = true;
        String src = "sql003_link_shp_blob.sql";
        SQL sql = this.getSQL(src);
        String sqlText = sql.getSqlText();
        sqlText = sqlText.replaceAll("&link_id", "" + this.getLinkId());
        boolean sqlDebug = false;
        QueryResult qr = this.getQueryResult(sqlText, sqlDebug);
        double[] coords = new double[0];
        if (qr.hasNext()) {
            BLOB mBlob;
            if (true) {
                Blob blob = (Blob) qr.getObject(2);
                mBlob = (oracle.sql.BLOB) blob;
            }
            long blobLength = mBlob.length();
            DataInputStream in = new DataInputStream(mBlob.getBinaryStream());
            byte byteOrder = in.readByte();
            int wkbType = in.readInt();
            int numPoints = in.readInt();
            this.debug("byteOrder:" + byteOrder, localDebug);
            this.debug("wkbType:" + wkbType, localDebug);
            this.debug("numPoints:" + numPoints, localDebug);
            int dataNo = numPoints * 2;
            coords = new double[dataNo];
            long longBits;
            for (int i = 0, iLen = dataNo; i < iLen; i++) {
                longBits = in.readLong();
                if (byteOrder == 1) {
                    longBits = Long.reverseBytes(longBits);
                }
                coords[i] = Double.longBitsToDouble(longBits);
            }
        }
        return coords;
    }

    private double[] loadShapeFromSdoGeometry() throws Exception {
        boolean localDebug = false;
        String src = "sql003_link_shp_sdo.sql";
        SQL sql = this.getSQL(src);
        String sqlText = sql.getSqlText();
        sqlText = sqlText.replaceAll("&link_id", "" + this.getLinkId());
        boolean sqlDebug = false;
        QueryResult qr = this.getQueryResult(sqlText, sqlDebug);
        double[] coords = null;
        if (qr.hasNext()) {
            STRUCT struct = (STRUCT) qr.getObject(2);
            SdoGeometry sdo = new SdoGeometry();
            coords = sdo.getCoordinates(struct);
            return coords;
        }
        return coords;
    }

    private LinkShape(long linkId) {
        super();
        this.linkId = linkId;
    }

    public static LinkShape getLinkShape(long linkId) {
        return new LinkShape(linkId);
    }

    private long linkId;

    private transient double[] coords;

    public static void main(String[] args) throws Exception {
        System.out.println("Hello....");
        LinkShapeList linkShapeList = new LinkShapeList();
        if (true) {
            LinkShape linkShape = new LinkShape(1190000100L);
            linkShapeList.add(linkShape);
        }
        if (true) {
            LinkShape linkShape = new LinkShape(1230003200L);
            linkShapeList.add(linkShape);
        }
        if (true) {
            KmlHandlerLinkShape kmlHandler = new KmlHandlerLinkShape();
            File file = new File("C:\\Documents and Settings\\sbmoon\\���� ȭ��\\kml\\linkShape.kml");
            kmlHandler.toKml(file, linkShapeList);
        }
        System.out.println("Good bye!");
    }
}
