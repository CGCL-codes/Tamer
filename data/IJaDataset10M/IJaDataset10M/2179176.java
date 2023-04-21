package org.tomac.tools.fix50sp2;

import java.nio.ByteBuffer;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.tomac.protocol.fix.messaging.*;

/**
 * @author seto
 *
 */
public class TestToFixPerformance {

    final int ITERATIONS = 1000000;

    final int DO_SAMPLE_DATA = 100;

    final long DISCARD_LEVEL = 7000L;

    FixMessageParser parser;

    FixMessageListener listener;

    FixNewOrderSingle order;

    FixMarketDataSnapshotFullRefresh message;

    ByteBuffer out;

    ByteBuffer buf;

    byte[] tmp;

    @Before
    public void setUp() {
        tmp = new byte[1024];
        out = ByteBuffer.allocate(1024);
        buf = ByteBuffer.allocate(1024);
        parser = new FixMessageParser();
        listener = new FixMessageListener() {

            @Override
            public void onFixNewOrderSingle(FixNewOrderSingle msg) {
                order = msg;
            }

            @Override
            public void onFixMarketDataSnapshotFullRefresh(FixMarketDataSnapshotFullRefresh msg) {
                message = msg;
            }

            @Override
            public void onFixHeartbeat(FixHeartbeat msg) {
            }

            @Override
            public void onFixTestRequest(FixTestRequest msg) {
            }

            @Override
            public void onFixResendRequest(FixResendRequest msg) {
            }

            @Override
            public void onFixReject(FixReject msg) {
            }

            @Override
            public void onFixSequenceReset(FixSequenceReset msg) {
            }

            @Override
            public void onFixLogout(FixLogout msg) {
            }

            @Override
            public void onFixIOI(FixIOI msg) {
            }

            @Override
            public void onFixAdvertisement(FixAdvertisement msg) {
            }

            @Override
            public void onFixExecutionReport(FixExecutionReport msg) {
            }

            @Override
            public void onFixOrderCancelReject(FixOrderCancelReject msg) {
            }

            @Override
            public void onFixLogon(FixLogon msg) {
            }

            @Override
            public void onFixNews(FixNews msg) {
            }

            @Override
            public void onFixEmail(FixEmail msg) {
            }

            @Override
            public void onFixNewOrderList(FixNewOrderList msg) {
            }

            @Override
            public void onFixOrderCancelRequest(FixOrderCancelRequest msg) {
            }

            @Override
            public void onFixOrderCancelReplaceRequest(FixOrderCancelReplaceRequest msg) {
            }

            @Override
            public void onFixOrderStatusRequest(FixOrderStatusRequest msg) {
            }

            @Override
            public void onFixAllocationInstruction(FixAllocationInstruction msg) {
            }

            @Override
            public void onFixListCancelRequest(FixListCancelRequest msg) {
            }

            @Override
            public void onFixListExecute(FixListExecute msg) {
            }

            @Override
            public void onFixListStatusRequest(FixListStatusRequest msg) {
            }

            @Override
            public void onFixListStatus(FixListStatus msg) {
            }

            @Override
            public void onFixAllocationInstructionAck(FixAllocationInstructionAck msg) {
            }

            @Override
            public void onFixDontKnowTradeDK(FixDontKnowTradeDK msg) {
            }

            @Override
            public void onFixQuoteRequest(FixQuoteRequest msg) {
            }

            @Override
            public void onFixQuote(FixQuote msg) {
            }

            @Override
            public void onFixSettlementInstructions(FixSettlementInstructions msg) {
            }

            @Override
            public void onFixMarketDataRequest(FixMarketDataRequest msg) {
            }

            @Override
            public void onFixMarketDataIncrementalRefresh(FixMarketDataIncrementalRefresh msg) {
            }

            @Override
            public void onFixMarketDataRequestReject(FixMarketDataRequestReject msg) {
            }

            @Override
            public void onFixQuoteCancel(FixQuoteCancel msg) {
            }

            @Override
            public void onFixQuoteStatusRequest(FixQuoteStatusRequest msg) {
            }

            @Override
            public void onFixMassQuoteAcknowledgement(FixMassQuoteAcknowledgement msg) {
            }

            @Override
            public void onFixSecurityDefinitionRequest(FixSecurityDefinitionRequest msg) {
            }

            @Override
            public void onFixSecurityDefinition(FixSecurityDefinition msg) {
            }

            @Override
            public void onFixSecurityStatusRequest(FixSecurityStatusRequest msg) {
            }

            @Override
            public void onFixSecurityStatus(FixSecurityStatus msg) {
            }

            @Override
            public void onFixTradingSessionStatusRequest(FixTradingSessionStatusRequest msg) {
            }

            @Override
            public void onFixTradingSessionStatus(FixTradingSessionStatus msg) {
            }

            @Override
            public void onFixMassQuote(FixMassQuote msg) {
            }

            @Override
            public void onFixBusinessMessageReject(FixBusinessMessageReject msg) {
            }

            @Override
            public void onFixBidRequest(FixBidRequest msg) {
            }

            @Override
            public void onFixBidResponse(FixBidResponse msg) {
            }

            @Override
            public void onFixListStrikePrice(FixListStrikePrice msg) {
            }

            @Override
            public void onFixRegistrationInstructions(FixRegistrationInstructions msg) {
            }

            @Override
            public void onFixRegistrationInstructionsResponse(FixRegistrationInstructionsResponse msg) {
            }

            @Override
            public void onFixOrderMassCancelRequest(FixOrderMassCancelRequest msg) {
            }

            @Override
            public void onFixOrderMassCancelReport(FixOrderMassCancelReport msg) {
            }

            @Override
            public void onFixNewOrderCross(FixNewOrderCross msg) {
            }

            @Override
            public void onFixCrossOrderCancelReplaceRequest(FixCrossOrderCancelReplaceRequest msg) {
            }

            @Override
            public void onFixCrossOrderCancelRequest(FixCrossOrderCancelRequest msg) {
            }

            @Override
            public void onFixSecurityTypeRequest(FixSecurityTypeRequest msg) {
            }

            @Override
            public void onFixSecurityTypes(FixSecurityTypes msg) {
            }

            @Override
            public void onFixSecurityListRequest(FixSecurityListRequest msg) {
            }

            @Override
            public void onFixSecurityList(FixSecurityList msg) {
            }

            @Override
            public void onFixDerivativeSecurityListRequest(FixDerivativeSecurityListRequest msg) {
            }

            @Override
            public void onFixDerivativeSecurityList(FixDerivativeSecurityList msg) {
            }

            @Override
            public void onFixNewOrderMultileg(FixNewOrderMultileg msg) {
            }

            @Override
            public void onFixMultilegOrderCancelReplace(FixMultilegOrderCancelReplace msg) {
            }

            @Override
            public void onFixTradeCaptureReportRequest(FixTradeCaptureReportRequest msg) {
            }

            @Override
            public void onFixTradeCaptureReport(FixTradeCaptureReport msg) {
            }

            @Override
            public void onFixOrderMassStatusRequest(FixOrderMassStatusRequest msg) {
            }

            @Override
            public void onFixQuoteRequestReject(FixQuoteRequestReject msg) {
            }

            @Override
            public void onFixRFQRequest(FixRFQRequest msg) {
            }

            @Override
            public void onFixQuoteStatusReport(FixQuoteStatusReport msg) {
            }

            @Override
            public void onFixQuoteResponse(FixQuoteResponse msg) {
            }

            @Override
            public void onFixConfirmation(FixConfirmation msg) {
            }

            @Override
            public void onFixPositionMaintenanceRequest(FixPositionMaintenanceRequest msg) {
            }

            @Override
            public void onFixPositionMaintenanceReport(FixPositionMaintenanceReport msg) {
            }

            @Override
            public void onFixRequestForPositions(FixRequestForPositions msg) {
            }

            @Override
            public void onFixRequestForPositionsAck(FixRequestForPositionsAck msg) {
            }

            @Override
            public void onFixPositionReport(FixPositionReport msg) {
            }

            @Override
            public void onFixTradeCaptureReportRequestAck(FixTradeCaptureReportRequestAck msg) {
            }

            @Override
            public void onFixTradeCaptureReportAck(FixTradeCaptureReportAck msg) {
            }

            @Override
            public void onFixAllocationReport(FixAllocationReport msg) {
            }

            @Override
            public void onFixAllocationReportAck(FixAllocationReportAck msg) {
            }

            @Override
            public void onFixConfirmation_Ack(FixConfirmation_Ack msg) {
            }

            @Override
            public void onFixSettlementInstructionRequest(FixSettlementInstructionRequest msg) {
            }

            @Override
            public void onFixAssignmentReport(FixAssignmentReport msg) {
            }

            @Override
            public void onFixCollateralRequest(FixCollateralRequest msg) {
            }

            @Override
            public void onFixCollateralAssignment(FixCollateralAssignment msg) {
            }

            @Override
            public void onFixCollateralResponse(FixCollateralResponse msg) {
            }

            @Override
            public void onFixCollateralReport(FixCollateralReport msg) {
            }

            @Override
            public void onFixCollateralInquiry(FixCollateralInquiry msg) {
            }

            @Override
            public void onFixNetworkCounterpartySystemStatusRequest(FixNetworkCounterpartySystemStatusRequest msg) {
            }

            @Override
            public void onFixNetworkCounterpartySystemStatusResponse(FixNetworkCounterpartySystemStatusResponse msg) {
            }

            @Override
            public void onFixUserRequest(FixUserRequest msg) {
            }

            @Override
            public void onFixUserResponse(FixUserResponse msg) {
            }

            @Override
            public void onFixCollateralInquiryAck(FixCollateralInquiryAck msg) {
            }

            @Override
            public void onFixConfirmationRequest(FixConfirmationRequest msg) {
            }

            @Override
            public void onFixContraryIntentionReport(FixContraryIntentionReport msg) {
            }

            @Override
            public void onFixSecurityDefinitionUpdateReport(FixSecurityDefinitionUpdateReport msg) {
            }

            @Override
            public void onFixSecurityListUpdateReport(FixSecurityListUpdateReport msg) {
            }

            @Override
            public void onFixAdjustedPositionReport(FixAdjustedPositionReport msg) {
            }

            @Override
            public void onFixAllocationInstructionAlert(FixAllocationInstructionAlert msg) {
            }

            @Override
            public void onFixExecutionAcknowledgement(FixExecutionAcknowledgement msg) {
            }

            @Override
            public void onFixTradingSessionList(FixTradingSessionList msg) {
            }

            @Override
            public void onFixTradingSessionListRequest(FixTradingSessionListRequest msg) {
            }

            @Override
            public void onFixSettlementObligationReport(FixSettlementObligationReport msg) {
            }

            @Override
            public void onFixDerivativeSecurityListUpdateReport(FixDerivativeSecurityListUpdateReport msg) {
            }

            @Override
            public void onFixTradingSessionListUpdateReport(FixTradingSessionListUpdateReport msg) {
            }

            @Override
            public void onFixMarketDefinitionRequest(FixMarketDefinitionRequest msg) {
            }

            @Override
            public void onFixMarketDefinition(FixMarketDefinition msg) {
            }

            @Override
            public void onFixMarketDefinitionUpdateReport(FixMarketDefinitionUpdateReport msg) {
            }

            @Override
            public void onFixUserNotification(FixUserNotification msg) {
            }

            @Override
            public void onFixOrderMassActionReport(FixOrderMassActionReport msg) {
            }

            @Override
            public void onFixOrderMassActionRequest(FixOrderMassActionRequest msg) {
            }

            @Override
            public void onFixApplicationMessageRequest(FixApplicationMessageRequest msg) {
            }

            @Override
            public void onFixApplicationMessageRequestAck(FixApplicationMessageRequestAck msg) {
            }

            @Override
            public void onFixApplicationMessageReport(FixApplicationMessageReport msg) {
            }

            @Override
            public void onFixStreamAssignmentRequest(FixStreamAssignmentRequest msg) {
            }

            @Override
            public void onFixStreamAssignmentReport(FixStreamAssignmentReport msg) {
            }

            @Override
            public void onFixStreamAssignmentReportACK(FixStreamAssignmentReportACK msg) {
            }

            @Override
            public void onFixPartyDetailsListRequest(FixPartyDetailsListRequest msg) {
            }

            @Override
            public void onFixPartyDetailsListReport(FixPartyDetailsListReport msg) {
            }

            @Override
            public void onUnknownMessageType(FixMessage msg) {
            }
        };
    }

    @Test
    public void testInBoundLatencyOrderEntry() throws Exception {
        byte[] strBuf = "8=FIXT.1.19=24135=D49=SenderCompId56=TargetCompId34=3752=20070223-22:28:3311=183338=140=244=1254=255=BHP48=BHP59=160=20060223-22:38:33526=362078=279=AllocACC180=1010.179=AllocACC280=2020.2453=2448=8447=D452=4448=AAA35354447=D452=310=089".getBytes();
        ByteBuffer buf = ByteBuffer.allocate(strBuf.length);
        buf.put(strBuf);
        buf.clear();
        int count = 0;
        long cumTime = 0L;
        long cumTimeIntervall = 0L;
        int sampleCount = 0;
        long sampleTime = 0L;
        System.out.println("toFIX testInBoundLatency");
        while (count < ITERATIONS) {
            long t0 = System.nanoTime();
            parser.parse(buf, listener);
            long t1 = System.nanoTime();
            cumTime += t1 - t0;
            cumTimeIntervall += t1 - t0;
            ++count;
            if (count % DO_SAMPLE_DATA == 0) {
                if (cumTimeIntervall / DO_SAMPLE_DATA < DISCARD_LEVEL) {
                    sampleCount++;
                    sampleTime += cumTimeIntervall / DO_SAMPLE_DATA;
                }
                cumTimeIntervall = 0L;
            }
            buf.flip();
        }
        if (sampleCount == 0) return;
        System.out.println("ns/msg\t#count\ttotns/msg\t#totCount");
        System.out.println(sampleTime / sampleCount + "\t" + sampleCount * DO_SAMPLE_DATA + "\t" + cumTime / ITERATIONS + "\t" + ITERATIONS);
    }

    @Test
    public void testInBoundLatencyMD() throws Exception {
        ByteBuffer buf = ByteBuffer.wrap("8=FIXT.1.19=15535=W34=249=ABFX52=20080722-16:37:11.23456=X2RV15=EUR/USD262=CAP00000112268=2269=1270=1.5786271=500000272=20080724269=1271=500000272=2008072410=218".getBytes());
        int count = 0;
        long cumTime = 0L;
        long cumTimeIntervall = 0L;
        int sampleCount = 0;
        long sampleTime = 0L;
        System.out.println("toFIX testInBoundLatency");
        while (count < ITERATIONS) {
            long t0 = System.nanoTime();
            parser.parse(buf, listener);
            long t1 = System.nanoTime();
            cumTime += t1 - t0;
            cumTimeIntervall += t1 - t0;
            ++count;
            if (count % DO_SAMPLE_DATA == 0) {
                if (cumTimeIntervall / DO_SAMPLE_DATA < DISCARD_LEVEL) {
                    sampleCount++;
                    sampleTime += cumTimeIntervall / DO_SAMPLE_DATA;
                }
                cumTimeIntervall = 0L;
            }
            buf.flip();
        }
        if (sampleCount == 0) return;
        System.out.println("ns/msg\t#count\ttotns/msg\t#totCount");
        System.out.println(sampleTime / sampleCount + "\t" + sampleCount * DO_SAMPLE_DATA + "\t" + cumTime / ITERATIONS + "\t" + ITERATIONS);
    }

    @Test
    public void testOutBoundLatencyOrderEntry() throws Exception {
        ByteBuffer buf = ByteBuffer.wrap("8=FIXT.1.19=24135=D49=SenderCompId56=TargetCompId34=3752=20070223-22:28:3311=183338=140=244=1254=255=BHP48=BHP59=160=20060223-22:38:33526=362078=279=AllocACC180=1010.179=AllocACC280=2020.2453=2448=8447=D452=4448=AAA35354447=D452=310=089".getBytes());
        parser.parse(buf, listener);
        ByteBuffer out = ByteBuffer.allocate(1024);
        int count = 0;
        long cumTime = 0L;
        long cumTimeIntervall = 0L;
        int sampleCount = 0;
        long sampleTime = 0L;
        System.out.println("toFIX testOutBoundLatency");
        while (count < ITERATIONS) {
            long t0 = System.nanoTime();
            order.encode(out);
            long t1 = System.nanoTime();
            out.clear();
            cumTime += t1 - t0;
            cumTimeIntervall += t1 - t0;
            order.clear();
            ++count;
            if (count % DO_SAMPLE_DATA == 0) {
                if (cumTimeIntervall / DO_SAMPLE_DATA < DISCARD_LEVEL) {
                    sampleCount++;
                    sampleTime += cumTimeIntervall / DO_SAMPLE_DATA;
                }
                cumTimeIntervall = 0L;
            }
        }
        System.out.println("ns/msg\t#count\ttotns/msg\t#totCount");
        System.out.println(sampleTime / sampleCount + "\t" + sampleCount * DO_SAMPLE_DATA + "\t" + cumTime / ITERATIONS + "\t" + ITERATIONS);
    }

    @Test
    public void testOutBoundLatencyMD() throws Exception {
        ByteBuffer buf = ByteBuffer.wrap("8=FIXT.1.19=15535=W34=249=ABFX52=20080722-16:37:11.23456=X2RV15=EUR/USD262=CAP00000112268=2269=1270=1.5786271=500000272=20080724269=1271=500000272=2008072410=218".getBytes());
        parser.parse(buf, listener);
        ByteBuffer out = ByteBuffer.allocate(1024);
        int count = 0;
        long cumTime = 0L;
        long cumTimeIntervall = 0L;
        int sampleCount = 0;
        long sampleTime = 0L;
        System.out.println("toFIX testOutBoundLatency");
        while (count < ITERATIONS) {
            long t0 = System.nanoTime();
            message.encode(out);
            long t1 = System.nanoTime();
            out.clear();
            cumTime += t1 - t0;
            cumTimeIntervall += t1 - t0;
            message.clear();
            ++count;
            if (count % DO_SAMPLE_DATA == 0) {
                if (cumTimeIntervall / DO_SAMPLE_DATA < DISCARD_LEVEL) {
                    sampleCount++;
                    sampleTime += cumTimeIntervall / DO_SAMPLE_DATA;
                }
                cumTimeIntervall = 0L;
            }
        }
        System.out.println("ns/msg\t#count\ttotns/msg\t#totCount");
        System.out.println(sampleTime / sampleCount + "\t" + sampleCount * DO_SAMPLE_DATA + "\t" + cumTime / ITERATIONS + "\t" + ITERATIONS);
    }

    public static void main(String[] args) {
        TestToFixPerformance perf = new TestToFixPerformance();
        try {
            perf.setUp();
            perf.testOutBoundLatencyMD();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
