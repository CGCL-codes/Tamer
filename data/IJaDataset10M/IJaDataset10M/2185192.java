package co.fxl.gui.api;

public interface IFlowPanel extends IPanel<IFlowPanel> {

    IFlowPanel spacing(int spacing);

    IFlowPanel addSpace(int space);

    IAlignment<IFlowPanel> align();
}
