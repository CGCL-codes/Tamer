package com.caimao.netflow.decoder.packetdecoder;

import com.caimao.netflow.packetdecoder.field.decoder.FieldTypeConstants;

public class DecodedPacketResult {

    int version = 5;

    int[] header;

    int[][] flows;

    int currentFlowCnt = 0;

    public int[][] getFlows() {
        return flows;
    }

    public void setFlows(int[][] flows) {
        this.flows = flows;
    }

    public int getCurrentFlowCnt() {
        return currentFlowCnt;
    }

    public void setCurrentFlowCnt(int currentFlowCnt) {
        this.currentFlowCnt = currentFlowCnt;
    }

    /**
	 * reverse search, inefficient
	 */
    int getHeaderFieldValue(int field) {
        if (field < 0) {
            return -1;
        }
        int headerIdx = PacketHeadDecoder.getHeaderFieldIdx(version, field);
        if (headerIdx < 0) {
            return -2;
        }
        return header[headerIdx];
    }

    /**
	 * TODO
	 * 
	 * @param flowIdx
	 * @param field
	 * @return
	 */
    int getFlowFieldValue(int flowIdx, int field) {
        if (flowIdx < flows.length) {
        }
        return -1;
    }

    void setFlowCount(int cnt) {
        if (flows != null && flows.length == cnt) {
            return;
        }
        flows = new int[cnt][];
    }

    void fillFlow(int[] decodedFlow) {
        flows[currentFlowCnt++] = decodedFlow;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public int[] getHeader() {
        return header;
    }

    public void setHeader(int[] header) {
        this.header = header;
    }

    @Override
    public String toString() {
        StringBuffer resultOutput = new StringBuffer("Header is: \n");
        for (String fieldName : FieldTypeConstants.fieldNames) {
            resultOutput.append(fieldName + ":" + getHeaderFieldValue(FieldTypeConstants.getFieldTypeCode(fieldName)) + "\n");
        }
        return resultOutput.toString();
    }
}
