package com.shine.Netflow.translator;

import java.util.ArrayList;
import java.util.List;
import com.shine.Netflow.model.RawNetFlow;
import com.shine.Netflow.utils.NetFlowUtil;
import com.shine.framework.core.util.DateUtil;

public class TranslatorV5 extends Translator {

    private static final int V5_HEADER_SIZE = 24;

    private static final int V5_TEMPLATE[][] = new int[][] { { V5_HEADER_SIZE + 0, 4 }, { V5_HEADER_SIZE + 4, 4 }, { V5_HEADER_SIZE + 8, 4 }, { V5_HEADER_SIZE + 12, 2 }, { V5_HEADER_SIZE + 14, 2 }, { V5_HEADER_SIZE + 16, 4 }, { V5_HEADER_SIZE + 20, 4 }, { V5_HEADER_SIZE + 24, 4 }, { V5_HEADER_SIZE + 28, 4 }, { V5_HEADER_SIZE + 32, 2 }, { V5_HEADER_SIZE + 34, 2 }, { V5_HEADER_SIZE + 36, 1 }, { V5_HEADER_SIZE + 37, 1 }, { V5_HEADER_SIZE + 38, 1 }, { V5_HEADER_SIZE + 39, 1 }, { V5_HEADER_SIZE + 40, 2 }, { V5_HEADER_SIZE + 42, 2 }, { V5_HEADER_SIZE + 44, 1 }, { V5_HEADER_SIZE + 45, 1 }, { V5_HEADER_SIZE + 46, 2 } };

    public RawNetFlow translate(final int rid, final byte[] buffer) {
        RawNetFlow flow = new RawNetFlow();
        flow.setRouterId(rid);
        flow.setSrcIP(NetFlowUtil.toLongNumber(buffer, V5_TEMPLATE[0][0], V5_TEMPLATE[0][1]));
        flow.setDstIP(NetFlowUtil.toLongNumber(buffer, V5_TEMPLATE[1][0], V5_TEMPLATE[1][1]));
        flow.setSrcPort(NetFlowUtil.toIntNumber(buffer, V5_TEMPLATE[9][0], V5_TEMPLATE[9][1]));
        flow.setDstPort(NetFlowUtil.toIntNumber(buffer, V5_TEMPLATE[10][0], V5_TEMPLATE[10][1]));
        flow.setInIf(NetFlowUtil.toIntNumber(buffer, V5_TEMPLATE[3][0], V5_TEMPLATE[3][1]));
        flow.setOutIf(NetFlowUtil.toIntNumber(buffer, V5_TEMPLATE[4][0], V5_TEMPLATE[4][1]));
        flow.setBytes(NetFlowUtil.toLongNumber(buffer, V5_TEMPLATE[6][0], V5_TEMPLATE[6][1]));
        flow.setProtocol(NetFlowUtil.toIntNumber(buffer, V5_TEMPLATE[13][0], V5_TEMPLATE[13][1]));
        flow.setLogTime(DateUtil.getCurrentTime());
        return flow;
    }
}
