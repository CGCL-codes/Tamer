package plecotus.models.bwin.doc.treegross;

class siofdistrib {

    /**  calcultes the site index of a generated a diameter distribution from stand values
      it uses all trees with no site index information at this point, parameters passed:
      the stand, species code, age, mean quadratic diameter [cm] and height [m] of dg  */
    void si(stand st, int code, int alter, double dg, double hg) {
        double d100 = 0;
        double h100 = 0;
        st.sortbyd();
        double size = 0.0;
        double siteindex = -9;
        size = st.area();
        int n100 = 0;
        n100 = (int) (100 * size);
        if (n100 == 0) n100 = 1;
        double gsum = 0.0;
        int n = 0;
        int i = 0;
        do {
            if (st.tr[i].code == code && st.tr[i].si == -9 && st.tr[i].out < 0) {
                n = n + 1;
                gsum = gsum + Math.PI * Math.pow(st.tr[i].d / 200, 2);
            }
            i = i + 1;
        } while (i < st.ntrees && n < n100);
        d100 = 200 * Math.sqrt(gsum / (Math.PI * n));
        uniformheight uh = new uniformheight();
        h100 = uh.height(d100, dg, hg, code);
        siteindex six = new siteindex();
        siteindex = six.getsiteindex(code, alter, h100);
        for (i = 0; i < st.ntrees; i++) if (st.tr[i].si == -9 && st.tr[i].code == code) st.tr[i].si = siteindex;
    }
}
