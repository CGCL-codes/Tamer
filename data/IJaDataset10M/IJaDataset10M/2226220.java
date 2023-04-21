package zzzhc.image.recognize.filter;

import zzzhc.image.recognize.Filter;
import zzzhc.image.recognize.RecognizeContext;

public class DropThinFilter implements Filter {

    private static final long serialVersionUID = 7575019609858050246L;

    public void doFilter(RecognizeContext context) {
        int w = context.getWidth();
        int h = context.getHeight();
        for (int x = 0; x < w; x++) {
            for (int y = 0; y < h; y++) {
                int p = context.getValue(x, y);
                if (p == 1) {
                    int p0 = context.getValue(x - 1, y);
                    int p1 = context.getValue(x + 1, y);
                    int p2 = context.getValue(x, y - 1);
                    int p3 = context.getValue(x, y + 1);
                    int p4 = context.getValue(x - 1, y - 1);
                    int p5 = context.getValue(x + 1, y + 1);
                    int p6 = context.getValue(x + 1, y - 1);
                    int p7 = context.getValue(x - 1, y + 1);
                    if ((p0 + p1) == 0) {
                        if (p2 + p3 + p4 + p5 + p6 + p7 < 3) {
                            context.setValue(x, y, 0);
                        }
                    }
                    if (p2 + p3 == 0) {
                        if (p0 + p1 + p4 + p5 + p6 + p7 < 3) {
                            context.setValue(x, y, 0);
                        }
                    }
                }
            }
        }
    }
}
