package plecotus.models.bwin.doc.treegross;

class siteindex {

    double hbon = 0.0;

    double hb0 = .0;

    double hb1 = 0.0;

    double hb2 = 0.0;

    double hb3 = 0.0;

    double hb4 = 0.0;

    double hb5 = 0.0;

    double ehoch = 0.0;

    String rtyp = " ";

    double ihpot = 0.0;

    /** set the coefficients for the species */
    void setcoefficient(int code) {
        if (code < 500) {
            hb0 = -75.65900;
            hb1 = 23.19200;
            hb2 = -1.46800;
            hb3 = 0.00000;
            hb4 = 0.21520;
            hb5 = 0.0;
            rtyp = "R";
        }
        if (code < 113) {
            hb0 = 1.2164;
            hb1 = 0.0194;
            hb2 = 1.1344;
            hb3 = 0.0;
            hb4 = 0.0;
            hb5 = 1.5500;
            rtyp = "C";
        }
        if (code == 113) {
            hb0 = 1.39521;
            hb1 = 0.03217;
            hb2 = 1.50327;
            hb3 = 0.00000;
            hb4 = 0.00000;
            hb5 = 1.88000;
            rtyp = "C";
        }
        if (code == 311) {
            hb0 = -46.04568;
            hb1 = 15.81886;
            hb2 = -1.33618;
            hb3 = 0.00000;
            hb4 = 0.22808;
            hb5 = 0.0;
            rtyp = "R";
        }
        if (code == 321 || code == 322) {
            hb0 = 0.62388;
            hb1 = 1.30296;
            hb2 = 296.04230;
            hb3 = 0.00000;
            hb4 = 0.00000;
            hb5 = 0.0;
            rtyp = "W";
        }
        if (code == 342) {
            hb0 = 0.49559;
            hb1 = 1.10113;
            hb2 = 96.17336;
            hb3 = 0.00000;
            hb4 = 0.00000;
            hb5 = 0.0;
            rtyp = "W";
        }
        if (code == 354) {
            hb0 = 0.80009;
            hb1 = 1.15093;
            hb2 = 84.18546;
            hb3 = 0.00000;
            hb4 = 0.00000;
            hb5 = 0.0;
            rtyp = "W";
        }
        if (code == 451) {
            hb0 = 0.23515;
            hb1 = 0.79707;
            hb2 = 66.99031;
            hb3 = 0.0;
            hb4 = 0.0;
            hb5 = 0.0;
            rtyp = "W";
        }
        if (code > 500) {
            hb0 = -49.87200;
            hb1 = 7.33090;
            hb2 = 0.77338;
            hb3 = 0.52684;
            hb4 = 0.10542;
            rtyp = "R";
        }
        if (code == 611) {
            hb0 = -47.09070;
            hb1 = 11.43220;
            hb2 = 0.00000;
            hb3 = 0.00000;
            hb4 = 0.20063;
            hb5 = 0.0;
            rtyp = "R";
        }
        if (code == 711) {
            hb0 = -31.67480;
            hb1 = 11.64500;
            hb2 = -1.04989;
            hb3 = -0.43221;
            hb4 = 0.31253;
            hb5 = 0.0;
            rtyp = "R";
        }
        if (code == 811) {
            hb0 = -0.53515;
            hb1 = 0.00000;
            hb2 = 0.00000;
            hb3 = -0.78758;
            hb4 = 0.38982;
            hb5 = 0.0;
            rtyp = "R";
        }
        if (code == 812) {
            hb0 = 1.88062;
            hb1 = 0.009296;
            hb2 = 0.6345;
            hb3 = 0.00000;
            hb4 = 0.0;
            hb5 = 0.0;
            rtyp = "C";
        }
    }

    /** this returns the height at the age of 100 years */
    double getsiteindex(int code, double age, double h100) {
        setcoefficient(code);
        if (rtyp == "C") {
            hbon = h100 / (hb0 * Math.exp(Math.log(1.0 - Math.exp(-hb1 * age)) * hb2));
        }
        if (rtyp == "R") {
            hbon = (h100 - hb0 - hb1 * Math.log(age) - hb2 * Math.pow(Math.log(age), 2.0)) / (hb3 + hb4 * Math.log(age));
        }
        if (rtyp == "W") {
            ehoch = ((-hb0) / ((hb1 - 1) * Math.exp(Math.log(100) * (hb1 - 1)))) + (hb0 / ((hb1 - 1) * Math.exp(Math.log(age) * (hb1 - 1))));
            ehoch = Math.exp(ehoch);
            hbon = hb2 * Math.exp(Math.log(h100 / hb2) * (1 / ehoch));
        }
        return hbon;
    }

    /** returns the potential height growth */
    double potheightgrowth(int code, double age, double h100, double hboni) {
        setcoefficient(code);
        if (rtyp == "R") {
            ihpot = (hb0 + hb1 * Math.log(age + 5) + hb2 * Math.pow(Math.log(age + 5), 2.0) + hb3 * hboni + hb4 * hboni * Math.log(age + 5)) - (hb0 + hb1 * Math.log(age) + hb2 * Math.pow(Math.log(age), 2.0) + hb3 * hboni + hb4 * hboni * Math.log(age));
        }
        if (rtyp == "C") {
            ihpot = (hb0 * hboni * Math.exp(Math.log(1 - Math.exp(-hb1 * (age + 5))) * hb2)) - (hb0 * hboni * Math.exp(Math.log(1 - Math.exp(-hb1 * (age))) * hb2));
        }
        if (rtyp == "W") {
            ihpot = 5 * ((hb0 * h100) / Math.exp(Math.log(age + 2) * hb1)) * Math.log(hb2 / h100);
        }
        return ihpot;
    }

    /** returns the index height for a given site index and age */
    double indexheight(int code, double age, double hboni) {
        double hpot = 0.0;
        setcoefficient(code);
        if (rtyp == "R") {
            hpot = hb0 + hb1 * Math.log(age) + hb2 * Math.pow(Math.log(age), 2.0) + hb3 * hboni + hb4 * hboni * Math.log(age);
        }
        if (rtyp == "C") {
            hpot = hb0 * hboni * Math.exp(Math.log(1 - Math.exp(-hb1 * (age))) * hb2);
        }
        if (rtyp == "W") {
            hpot = hb2 * Math.pow((hboni / hb2), Math.exp(-hb0 / ((hb1 - 1) * Math.pow(100, (hb1 - 1))) + hb0 / ((hb1 - 1) * Math.pow(age, (hb1 - 1)))));
        }
        return hpot;
    }
}
