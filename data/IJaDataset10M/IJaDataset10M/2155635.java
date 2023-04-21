package edu.ksu.cis.bnj.gui.tools;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import edu.ksu.cis.util.GlobalOptions;

public class Constants {

    public int CPF_CellPadding = 8;

    public int CPF_CellSpacing = 10;

    public ColorWheel Colors;

    public FontWheel Fonts;

    public int TranslateVectorStyle = SWT.LINE_DASH;

    public int EdgeWidth = 1;

    public int EdgeStyle = SWT.LINE_SOLID;

    public int EdgeWidthSelected = 2;

    public int EdgeStyleSelected = SWT.LINE_SOLID;

    public int EdgeTempWidth = 1;

    public int EdgeTempStyle = SWT.LINE_DASH;

    public int CursorLineStyle = SWT.LINE_DOT;

    public int CursorLen = 10;

    public int LeftMouse = 1;

    public int RightMouse = 3;

    public int DragDifferential = 6;

    public int ArrowLen = 15;

    public int RoadMapLength = 1000;

    public double RoadMapRatio = 0.16666666666666666666666666666667;

    public double RoadMapRatioArrow = 0.25;

    public int TranslationFrameSkip = 3;

    public int DragFrameSkip = 2;

    public double ScalingFactor = 1.07;

    public double EnableCPF_Xf = -.45;

    public double EnableCPF_Yf = 1.0;

    public int EnableCPF_D = 12;

    public int GridLineStyle = SWT.LINE_SOLID;

    public int GridLineWidth = 1;

    public int GridDistance = 50;

    public boolean RenderNetworkBoundingBox = false;

    public int TranslationDelta = 10;

    public int CircleLayoutLen = 25;

    public double VisNodeOrderPlacement_Xf = .35;

    public double VisNodeOrderPlacement_Yf = .35;

    public Constants() {
        Colors = new ColorWheel();
        Fonts = new FontWheel();
    }

    public void init(Display disp) {
        GlobalOptions GO = GlobalOptions.getInstance();
        TranslateVectorStyle = GO.getInteger("swt_translatevector_linestyle", SWT.LINE_DASH);
        CursorLineStyle = GO.getInteger("swt_cursor_linestyle", SWT.LINE_DOT);
        GridLineStyle = GO.getInteger("swt_grid_linestyle", SWT.LINE_SOLID);
        EdgeStyle = GO.getInteger("swt_edge_linestyle", SWT.LINE_SOLID);
        EdgeStyleSelected = GO.getInteger("swt_edge_selected_linestyle", SWT.LINE_SOLID);
        EdgeTempStyle = GO.getInteger("swt_edgetemp_linestyle", SWT.LINE_DASH);
        CPF_CellPadding = GO.getInteger("cpf_cellpadding", 8);
        CPF_CellSpacing = GO.getInteger("cpf_cellspacing", 10);
        EdgeWidth = GO.getInteger("edge_width", 1);
        EdgeWidthSelected = GO.getInteger("edge_width_selected", 2);
        EdgeTempWidth = GO.getInteger("edge_temp_width", 1);
        CursorLen = GO.getInteger("cursor_len", 10);
        LeftMouse = GO.getInteger("left_mouse", 1);
        RightMouse = GO.getInteger("right_mouse", 3);
        DragDifferential = GO.getInteger("drag_differential", 6);
        ArrowLen = GO.getInteger("arrow_len", 15);
        RoadMapLength = GO.getInteger("road_map_len", 1000);
        TranslationFrameSkip = GO.getInteger("translation_frame_skip", 3);
        DragFrameSkip = GO.getInteger("drag_frame_skip", 2);
        EnableCPF_D = GO.getInteger("enable_cpf_dim", 12);
        GridLineWidth = GO.getInteger("grid_linewidth", 2);
        GridDistance = GO.getInteger("grid_distance", 50);
        TranslationDelta = GO.getInteger("translation_delta", 10);
        CircleLayoutLen = GO.getInteger("circle_layout_len", 25);
        RenderNetworkBoundingBox = GO.getBoolean("render_boundingbox", false);
        RoadMapRatio = GO.getDouble("roadmap_ratio", 0.16666666666666666666666666666667);
        RoadMapRatioArrow = GO.getDouble("roadmap_ratioarrow", 0.25);
        ScalingFactor = GO.getDouble("interactive_scalingfactor", 1.07);
        EnableCPF_Xf = GO.getDouble("enable_cpf_xf", -.20);
        EnableCPF_Yf = GO.getDouble("enable_cpf_yf", .65);
        VisNodeOrderPlacement_Xf = GO.getDouble("vis_nodeorder_xf", .35);
        VisNodeOrderPlacement_Yf = GO.getDouble("vis_nodeorder_yf", .5);
        Colors.init(disp);
        Fonts.init(disp);
    }

    public void clean() {
        Colors.clean();
        Fonts.clean();
    }
}
