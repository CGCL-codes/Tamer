package org.jscsi.scsi.protocol.mode;

@SuppressWarnings("unchecked")
public class StaticModePageRegistry extends ModePageRegistry {

    public StaticModePageRegistry() {
        super();
    }

    @Override
    protected void populateModePages() {
        this.populateCaching();
        this.populateControl();
        this.populateControlExtension();
        this.populateInformationalExceptionsControl();
        this.populateReadWriteErrorRecovery();
        register(Caching.PAGE_CODE, caching);
        register(Control.PAGE_CODE, control);
        register(ControlExtension.PAGE_CODE, ControlExtension.SUBPAGE_CODE, controlExtension);
        register(InformationalExceptionsControl.PAGE_CODE, informationalExceptionsControl);
        register(ReadWriteErrorRecovery.PAGE_CODE, readWriteErrorRecovery);
    }

    protected void populateCaching() {
        Caching page = new Caching();
        page.setIC(false);
        page.setABPF(true);
        page.setCAP(false);
        page.setDISC(true);
        page.setSIZE(false);
        page.setWCE(false);
        page.setMF(false);
        page.setRCD(true);
        page.setDemandReadRetentionPriority(0x00);
        page.setWriteRetentionPriority(0x00);
        page.setDisablePrefetchTransferLength(0);
        page.setMinimumPrefetch(0);
        page.setMaximumPrefetch(0);
        page.setMaximumPrefetchCeiling(0);
        page.setFSW(false);
        page.setLBCSS(false);
        page.setDRA(false);
        page.setNV_DIS(false);
        page.setNumberOfCacheSegments(0);
        page.setCacheSegmentSize(0);
        this.setCaching(caching);
    }

    protected void populateControl() {
        Control page = new Control();
        page.setTST(0x0);
        page.setTMF_ONLY(false);
        page.setD_SENSE(false);
        page.setGLTSD(true);
        page.setRLEC(false);
        page.setQueueAlgorithmModifier(0x01);
        page.setQERR(0x00);
        page.setRAC(false);
        page.setUA_INTLCK_CTRL(0x00);
        page.setSWP(false);
        page.setATO(false);
        page.setTAS(false);
        page.setAutoloadMode(0x00);
        page.setBusyTimeoutPeriod(0xFFFF);
        page.setExtendedSelfTestCompletionTime(587);
        this.setControl(page);
    }

    protected void populateInformationalExceptionsControl() {
        InformationalExceptionsControl page = new InformationalExceptionsControl();
        page.setPERF(true);
        page.setEBF(true);
        page.setEWASC(false);
        page.setDEXCPT(false);
        page.setTEST(false);
        page.setLOGERR(false);
        page.setMRIE(0x00);
        page.setIntervalTimer(0);
        page.setReportCount(0);
        this.setInformationalExceptionsControl(page);
    }

    protected void populateReadWriteErrorRecovery() {
        ReadWriteErrorRecovery page = new ReadWriteErrorRecovery();
        page.setAWRE(true);
        page.setARRE(false);
        page.setTB(false);
        page.setRC(false);
        page.setEER(false);
        page.setPER(false);
        page.setDTE(false);
        page.setDCR(false);
        page.setReadRetryCount(0);
        page.setWriteRetryCount(0);
        page.setRecoveryTimeLimit(0);
        this.setReadWriteErrorRecovery(page);
    }

    protected void populateControlExtension() {
        ControlExtension page = new ControlExtension();
        page.setTCMOS(false);
        page.setSCSIP(false);
        page.setIALUAE(false);
        page.setInitialPriority(0);
        this.setControlExtension(page);
    }
}
