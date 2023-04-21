package com.ynhenc.kmlexport;

import com.ynhenc.comm.db.*;

public class RecordNode {

    public RecordNode(QueryResult qr) {
        this.RNO = qr.getInteger("RNO");
        this.ROAD_ROADRANK_CD = qr.getInteger("ROAD#ROADRANK$CD");
        this.ROADRANK_DESC = qr.getString("ROADRANK_DESC");
        this.ROAD_NO = qr.getInteger("ROAD_NO");
        this.ROAD_NAME = qr.getString("ROAD_NAME");
        this.NODE_NODETYPE_CD = qr.getInteger("NODE#NODETYPE$CD");
        this.NODETYPE_DESC = qr.getString("NODETYPE_DESC");
        this.NODE_TURNP_CD = qr.getInteger("NODE#TURN$CD");
        this.TURNP_DESC = qr.getString("TURNP_DESC");
        this.NODE_NODESIGN_CD = qr.getInteger("NODE#NODESIGN$CD");
        this.NODESIGN_DESC = qr.getString("NODESIGN_DESC");
        this.NODE_ID = qr.getInteger("NODE$ID");
        this.NODE_NAME = qr.getString("NODE_NAME");
        this.NODE_GX = qr.getDouble("NODE_GX");
        this.NODE_GY = qr.getDouble("NODE_GY");
        this.LINK_FT_NODE_ID = qr.getInteger("LINK_FT#NODE$ID");
    }

    Integer RNO = 0;

    Integer ROAD_ROADRANK_CD = 0;

    String ROADRANK_DESC = "";

    Integer ROAD_NO = 0;

    String ROAD_NAME = "";

    Integer NODE_NODETYPE_CD = 0;

    String NODETYPE_DESC = "";

    Integer NODE_TURNP_CD = 0;

    String TURNP_DESC = "";

    Integer NODE_NODESIGN_CD = 0;

    String NODESIGN_DESC = "";

    Integer NODE_ID = 0;

    String NODE_NAME = "";

    Double NODE_GX = 0.0;

    Double NODE_GY = 0.0;

    Integer LINK_FT_NODE_ID = 0;
}
