package eibstack.layer7;

import eibstack.layer4.T_BroadcastService;
import eibstack.transceiver.TFrame;

public class A_BroadcastImpl implements A_BroadcastService, T_BroadcastService.Listener {

    private T_BroadcastService tbs;

    private Listener abl;

    public A_BroadcastImpl(T_BroadcastService tbs) {
        this.tbs = tbs;
        tbs.setListener(this);
    }

    public void setListener(Listener l) {
        abl = l;
    }

    public int physicalAddress_ReadReq(int pr, int hc, boolean reqL2Ack, boolean waitL2Con) {
        byte[] aPDU = APDU.makeNoParamsReq(APCI.PHYSADDR_READ);
        return tbs.broadcast_Req(pr, hc, reqL2Ack, aPDU, waitL2Con);
    }

    public int physicalAddress_ReadRes(int pr, int hc, boolean reqL2Ack, boolean waitL2Con) {
        byte[] aPDU = APDU.makeNoParamsReq(APCI.PHYSADDR_RES);
        return tbs.broadcast_Req(pr, hc, reqL2Ack, aPDU, waitL2Con);
    }

    public int physicalAddress_WriteReq(int pr, int hc, boolean reqL2Ack, int physAddr, boolean waitL2Con) {
        byte[] aPDU = APDU.makeAddress(APCI.PHYSADDR_WRITE, physAddr);
        return tbs.broadcast_Req(pr, hc, reqL2Ack, aPDU, waitL2Con);
    }

    public int physAddressSerNo_ReadReq(int pr, int hc, boolean reqL2Ack, long serNo, boolean waitL2Con) {
        byte[] aPDU = APDU.makePhysAddrSerNoRead(serNo);
        return tbs.broadcast_Req(pr, hc, reqL2Ack, aPDU, waitL2Con);
    }

    public int physAddressSerNo_ReadRes(int pr, int hc, boolean reqL2Ack, long serNo, int domainAddr, boolean waitL2Con) {
        byte[] aPDU = APDU.makePhysAddrSerNoRes(serNo, domainAddr);
        return tbs.broadcast_Req(pr, hc, reqL2Ack, aPDU, waitL2Con);
    }

    public int physAddressSerNo_WriteReq(int pr, int hc, boolean reqL2Ack, long serNo, int physAddr, boolean waitL2Con) {
        byte[] aPDU = APDU.makePhysAddrSerNoWrite(serNo, physAddr);
        return tbs.broadcast_Req(pr, hc, reqL2Ack, aPDU, waitL2Con);
    }

    public int domainAddress_ReadReq(int pr, int hc, boolean reqL2Ack, boolean waitL2Con) {
        byte[] aPDU = APDU.makeNoParamsReq(APCI.DOMAINADDR_READ);
        return tbs.broadcast_Req(pr, hc, reqL2Ack, aPDU, waitL2Con);
    }

    public int domainAddress_ReadRes(int pr, int hc, boolean reqL2Ack, int domainAddr, boolean waitL2Con) {
        byte[] aPDU = APDU.makeAddress(APCI.DOMAINADDR_RES, domainAddr);
        return tbs.broadcast_Req(pr, hc, reqL2Ack, aPDU, waitL2Con);
    }

    public int domainAddress_WriteReq(int pr, int hc, boolean reqL2Ack, int domainAddr, boolean waitL2Con) {
        byte[] aPDU = APDU.makeAddress(APCI.DOMAINADDR_WRITE, domainAddr);
        return tbs.broadcast_Req(pr, hc, reqL2Ack, aPDU, waitL2Con);
    }

    public int domainAddrSel_ReadReq(int pr, int hc, boolean reqL2Ack, int domainAddr, int startAddr, int range, boolean waitL2Con) {
        byte[] aPDU = APDU.makeDomainAddrSelRead(domainAddr, startAddr, range);
        return tbs.broadcast_Req(pr, hc, reqL2Ack, aPDU, waitL2Con);
    }

    public int serviceInfo_WriteReq(int pr, int hc, boolean reqL2Ack, int info, boolean waitL2Con) {
        byte[] aPDU = APDU.makeServiceInfoWrite(info);
        return tbs.broadcast_Req(pr, hc, reqL2Ack, aPDU, waitL2Con);
    }

    public void broadcast_Ind(int sa, int pr, int hc, byte[] tSDU) {
        if (abl == null) return;
        int length = tSDU.length - TFrame.MIN_LENGTH;
        if (length < 1) return;
        int apci = ((tSDU[TFrame.APDU_START + 0] & 0x03) << 24) + ((tSDU[TFrame.APDU_START + 1] & 0xFF) << 16);
        switch(apci & APCI._4) {
            case APCI.PHYSADDR_WRITE:
                if (length == 3) {
                    int physAddr = APDU.getAddressAddr(tSDU);
                    abl.physicalAddress_WriteInd(sa, pr, hc, physAddr);
                }
                return;
            case APCI.PHYSADDR_READ:
                if (length == 1) {
                    abl.physicalAddress_ReadInd(sa, pr, hc);
                }
                return;
            case APCI.PHYSADDR_RES:
                if (length == 1) {
                    abl.physicalAddress_ReadCon(sa, pr, hc);
                }
                return;
        }
        switch(apci & APCI._10) {
            case APCI.PHYSADDRSERNO_READ:
                if (length == 7) {
                    long serNo = APDU.getPhysAddrSerNoSerNo(tSDU);
                    abl.physAddressSerNo_ReadInd(sa, pr, hc, serNo);
                }
                return;
            case APCI.PHYSADDRSERNO_RES:
                if (length == 11) {
                    long serNo = APDU.getPhysAddrSerNoSerNo(tSDU);
                    int domainAddr = APDU.getPhysAddrSerNoAddress(tSDU);
                    abl.physAddressSerNo_ReadCon(sa, pr, hc, serNo, domainAddr);
                }
                return;
            case APCI.PHYSADDRSERNO_WRITE:
                if (length == 13) {
                    long serNo = APDU.getPhysAddrSerNoSerNo(tSDU);
                    int physAddr = APDU.getPhysAddrSerNoAddress(tSDU);
                    abl.physAddressSerNo_WriteInd(sa, pr, hc, serNo, physAddr);
                }
                return;
            case APCI.SERVICEINFO_WRITE:
                if (length == 4) {
                    int info = APDU.getServiceInfoWriteInfo(tSDU);
                    abl.serviceInfo_WriteInd(sa, pr, hc, info);
                }
                return;
            case APCI.DOMAINADDR_WRITE:
                if (length == 3) {
                    int domainAddr = APDU.getAddressAddr(tSDU);
                    abl.domainAddress_WriteInd(sa, pr, hc, domainAddr);
                }
                return;
            case APCI.DOMAINADDR_READ:
                if (length == 1) {
                    abl.domainAddress_ReadInd(sa, pr, hc);
                }
                return;
            case APCI.DOMAINADDR_RES:
                if (length == 3) {
                    int domainAddr = APDU.getAddressAddr(tSDU);
                    abl.domainAddress_ReadCon(sa, pr, hc, domainAddr);
                }
                return;
            case APCI.DOMAINADDRSEL_READ:
                if (length == 6) {
                    int domainAddr = APDU.getDomainAddrSelReadDomainAddr(tSDU);
                    int startAddr = APDU.getDomainAddrSelReadStartAddr(tSDU);
                    int range = APDU.getDomainAddrSelReadRange(tSDU);
                    abl.domainAddrSel_ReadInd(sa, pr, hc, domainAddr, startAddr, range);
                }
                return;
        }
    }
}
