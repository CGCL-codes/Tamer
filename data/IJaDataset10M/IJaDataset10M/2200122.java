package com.ivis.xprocess.ui.diagram.print;

public interface PrintPreferences {

    final String FIT_TO_PAGE = "fit_to_page";

    final String PAPER_SIZE = "paper_size";

    final String PRINT_BORDER = "print_border";

    final String PRINT_PAGE_NUMBERS = "print_page_numbers";

    final String PRINT_DIAGRAM_NAME = "print_diagram_name";

    final String ORIENTATION = "orientation";

    final String ZOOM = "zoom";

    final String CUSTOM_PAPER = "custom_paper";

    final String CUSTOM_PAPER_SIZE_WIDTH = "custom_width";

    final String CUSTOM_PAPER_SIZE_HEIGHT = "custom_height";

    final String LEFT_MARGIN = "left_margin";

    final String TOP_MARGIN = "top_margin";

    final String RIGHT_MARGIN = "right_margin";

    final String BOTTOM_MARGIN = "bottom_margin";

    interface Orientation {

        final int PORTRAIT = 1;

        final int LANDSCAPE = 2;
    }

    interface PaperSize {

        final String LETTER = "letter";

        final String LEGAL = "legal";

        final String EXECUTIVE = "executive";

        final String ENV_DL = "EnvDL";

        final String A0 = "A0";

        final String A1 = "A1";

        final String A2 = "A2";

        final String A3 = "A3";

        final String A4 = "A4";

        final String A5 = "A5";

        final String B5 = "B5";

        final String A = "A";

        final String B = "B";

        final String C = "C";

        final String D = "D";

        final String E = "E";
    }
}
