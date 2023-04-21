package uk.me.g4dpz.satellite;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * @author g4dpz
 * 
 */
public class LEOSatellite extends Satellite {

    private double aodp;

    private double aycof;

    private double c1;

    private double c4;

    private double c5;

    private double cosio;

    private double d2;

    private double d3;

    private double d4;

    private double delmo;

    private double omgcof;

    private double eta;

    private double omgdot;

    private double sinio;

    private double xnodp;

    private double sinmo;

    private double t2cof;

    private double t3cof;

    private double t4cof;

    private double t5cof;

    private double x1mth2;

    private double x3thm1;

    private double x7thm1;

    private double xmcof;

    private double xmdot;

    private double xnodcf;

    private double xnodot;

    private double xlcof;

    private boolean sgp4Init;

    private boolean sgp4Simple;

    private static Log logger = LogFactory.getLog(LEOSatellite.class);

    /**
     * Creates a Low Earth Orbit Satellite.
     * 
     * @param tle the three line elements
     */
    public LEOSatellite(final TLE tle) {
        super(tle);
        sgp4Init = false;
        sgp4Simple = false;
        aodp = 0.0;
        aycof = 0.0;
        c1 = 0.0;
        c4 = 0.0;
        c5 = 0.0;
        cosio = 0.0;
        d2 = 0.0;
        d3 = 0.0;
        d4 = 0.0;
        delmo = 0.0;
        omgcof = 0.0;
        eta = 0.0;
        omgdot = 0.0;
        sinio = 0.0;
        xnodp = 0.0;
        sinmo = 0.0;
        t2cof = 0.0;
        t3cof = 0.0;
        t4cof = 0.0;
        t5cof = 0.0;
        x1mth2 = 0.0;
        x3thm1 = 0.0;
        x7thm1 = 0.0;
        xmcof = 0.0;
        xmdot = 0.0;
        xnodcf = 0.0;
        xnodot = 0.0;
        xlcof = 0.0;
    }

    @Override
    protected synchronized void calculateSGP4(final double tsince, final Vector4 position, final Vector4 velocity, final SatPos satPos) {
        double temp;
        double temp1;
        double temp2;
        double temp3;
        double temp4;
        double temp5;
        double temp6;
        int i;
        if (!sgp4Init) {
            sgp4Init = true;
            final double a1 = Math.pow(XKE / getTLE().getXno(), TWO_THIRDS);
            cosio = Math.cos(getTLE().getXincl());
            final double theta2 = cosio * cosio;
            x3thm1 = 3 * theta2 - 1.0;
            final double eosq = getTLE().getEo() * getTLE().getEo();
            final double betao2 = 1.0 - eosq;
            final double betao = Math.sqrt(betao2);
            final double del1 = 1.5 * CK2 * x3thm1 / (a1 * a1 * betao * betao2);
            final double ao = a1 * (1.0 - del1 * (0.5 * TWO_THIRDS + del1 * (1.0 + 134.0 / 81.0 * del1)));
            final double delo = 1.5 * CK2 * x3thm1 / (ao * ao * betao * betao2);
            xnodp = getTLE().getXno() / (1.0 + delo);
            aodp = ao / (1.0 - delo);
            sgp4Simple = (aodp * (1 - getTLE().getEo()) / AE) < (220 / EARTH_RADIUS_KM + AE);
            setPerigee((aodp * (1 - getTLE().getEo()) - AE) * EARTH_RADIUS_KM);
            checkPerigee();
            final double pinvsq = 1 / (aodp * aodp * betao2 * betao2);
            final double tsi = 1 / (aodp - getS4());
            eta = aodp * getTLE().getEo() * tsi;
            final double etasq = eta * eta;
            final double eeta = getTLE().getEo() * eta;
            final double psisq = Math.abs(1 - etasq);
            final double coef = getQoms24() * Math.pow(tsi, 4);
            final double coef1 = coef / Math.pow(psisq, 3.5);
            final double c2 = coef1 * xnodp * (aodp * (1 + 1.5 * etasq + eeta * (4 + etasq)) + 0.75 * CK2 * tsi / psisq * x3thm1 * (8 + 3 * etasq * (8 + etasq)));
            c1 = getTLE().getBstar() * c2;
            sinio = Math.sin(getTLE().getXincl());
            final double a3ovk2 = -J3_HARMONIC / CK2 * Math.pow(AE, 3);
            final double c3 = coef * tsi * a3ovk2 * xnodp * AE * sinio / getTLE().getEo();
            x1mth2 = 1 - theta2;
            c4 = 2 * xnodp * coef1 * aodp * betao2 * (eta * (2 + 0.5 * etasq) + getTLE().getEo() * (0.5 + 2 * etasq) - 2 * CK2 * tsi / (aodp * psisq) * (-3 * x3thm1 * (1 - 2 * eeta + etasq * (1.5 - 0.5 * eeta)) + 0.75 * x1mth2 * (2 * etasq - eeta * (1 + etasq)) * Math.cos(2 * getTLE().getOmegao())));
            c5 = 2 * coef1 * aodp * betao2 * (1 + 2.75 * (etasq + eeta) + eeta * etasq);
            final double theta4 = theta2 * theta2;
            temp1 = 3 * CK2 * pinvsq * xnodp;
            temp2 = temp1 * CK2 * pinvsq;
            temp3 = 1.25 * CK4 * pinvsq * pinvsq * xnodp;
            xmdot = xnodp + 0.5 * temp1 * betao * x3thm1 + 0.0625 * temp2 * betao * (13 - 78 * theta2 + 137 * theta4);
            final double x1m5th = 1 - 5 * theta2;
            omgdot = -0.5 * temp1 * x1m5th + 0.0625 * temp2 * (7 - 114 * theta2 + 395 * theta4) + temp3 * (3 - 36 * theta2 + 49 * theta4);
            final double xhdot1 = -temp1 * cosio;
            xnodot = xhdot1 + (0.5 * temp2 * (4 - 19 * theta2) + 2 * temp3 * (3 - 7 * theta2)) * cosio;
            omgcof = getTLE().getBstar() * c3 * Math.cos(getTLE().getOmegao());
            xmcof = -TWO_THIRDS * coef * getTLE().getBstar() * AE / eeta;
            xnodcf = 3.5 * betao2 * xhdot1 * c1;
            t2cof = 1.5 * c1;
            xlcof = 0.125 * a3ovk2 * sinio * (3 + 5 * cosio) / (1 + cosio);
            aycof = 0.25 * a3ovk2 * sinio;
            delmo = Math.pow(1 + eta * Math.cos(getTLE().getXmo()), 3);
            sinmo = Math.sin(getTLE().getXmo());
            x7thm1 = 7 * theta2 - 1;
            if (!sgp4Simple) {
                final double c1sq = c1 * c1;
                d2 = 4 * aodp * tsi * c1sq;
                temp = d2 * tsi * c1 / 3;
                d3 = (17 * aodp + getS4()) * temp;
                d4 = 0.5 * temp * aodp * tsi * (221 * aodp + 31 * getS4()) * c1;
                t3cof = d2 + 2 * c1sq;
                t4cof = 0.25 * (3 * d3 + c1 * (12 * d2 + 10 * c1sq));
                t5cof = 0.2 * (3 * d4 + 12 * c1 * d3 + 6 * d2 * d2 + 15 * c1sq * (2 * d2 + c1sq));
            }
        }
        final double xmdf = getTLE().getXmo() + xmdot * tsince;
        final double omgadf = getTLE().getOmegao() + omgdot * tsince;
        final double xnoddf = getTLE().getXnodeo() + xnodot * tsince;
        double omega = omgadf;
        double xmp = xmdf;
        final double tsq = tsince * tsince;
        final double xnode = xnoddf + xnodcf * tsq;
        double tempa = 1 - c1 * tsince;
        double tempe = getTLE().getBstar() * c4 * tsince;
        double templ = t2cof * tsq;
        if (!sgp4Simple) {
            final double delomg = omgcof * tsince;
            final double delm = xmcof * (Math.pow(1 + eta * Math.cos(xmdf), 3) - delmo);
            temp = delomg + delm;
            xmp = xmdf + temp;
            omega = omgadf - temp;
            final double tcube = tsq * tsince;
            final double tfour = tsince * tcube;
            tempa = tempa - d2 * tsq - d3 * tcube - d4 * tfour;
            tempe = tempe + getTLE().getBstar() * c5 * (Math.sin(xmp) - sinmo);
            templ = templ + t3cof * tcube + tfour * (t4cof + tsince * t5cof);
        }
        final double a = aodp * Math.pow(tempa, 2);
        final double e = getTLE().getEo() - tempe;
        final double xl = xmp + omega + xnode + xnodp * templ;
        final double beta = Math.sqrt(1 - e * e);
        final double xn = XKE / Math.pow(a, 1.5);
        final double axn = e * Math.cos(omega);
        temp = 1 / (a * beta * beta);
        final double xll = temp * xlcof * axn;
        final double aynl = temp * aycof;
        final double xlt = xl + xll;
        final double ayn = e * Math.sin(omega) + aynl;
        final double capu = mod2PI(xlt - xnode);
        temp2 = capu;
        i = 0;
        double sinepw = 0.0;
        double cosepw = 0.0;
        do {
            sinepw = Math.sin(temp2);
            cosepw = Math.cos(temp2);
            temp3 = axn * sinepw;
            temp4 = ayn * cosepw;
            temp5 = axn * cosepw;
            temp6 = ayn * sinepw;
            final double epw = (capu - temp4 + temp3 - temp2) / (1 - temp5 - temp6) + temp2;
            if (Math.abs(epw - temp2) <= EPSILON) {
                break;
            }
            temp2 = epw;
        } while (i++ < 10);
        final double ecose = temp5 + temp6;
        final double esine = temp3 - temp4;
        final double elsq = axn * axn + ayn * ayn;
        temp = 1 - elsq;
        final double pl = a * temp;
        final double r = a * (1 - ecose);
        temp1 = 1 / r;
        final double rdot = XKE * Math.sqrt(a) * esine * temp1;
        final double rfdot = XKE * Math.sqrt(pl) * temp1;
        temp2 = a * temp1;
        final double betal = Math.sqrt(temp);
        temp3 = 1 / (1 + betal);
        final double cosu = temp2 * (cosepw - axn + ayn * esine * temp3);
        final double sinu = temp2 * (sinepw - ayn - axn * esine * temp3);
        final double u = Math.atan2(sinu, cosu);
        final double sin2u = 2 * sinu * cosu;
        final double cos2u = 2 * cosu * cosu - 1;
        temp = 1 / pl;
        temp1 = CK2 * temp;
        temp2 = temp1 * temp;
        final double rk = r * (1 - 1.5 * temp2 * betal * x3thm1) + 0.5 * temp1 * x1mth2 * cos2u;
        final double uk = u - 0.25 * temp2 * x7thm1 * sin2u;
        final double xnodek = xnode + 1.5 * temp2 * cosio * sin2u;
        final double xinck = getTLE().getXincl() + 1.5 * temp2 * cosio * sinio * cos2u;
        final double rdotk = rdot - xn * temp1 * x1mth2 * sin2u;
        final double rfdotk = rfdot + xn * temp1 * (x1mth2 * cos2u + 1.5 * x3thm1);
        final double sinuk = Math.sin(uk);
        final double cosuk = Math.cos(uk);
        final double sinik = Math.sin(xinck);
        final double cosik = Math.cos(xinck);
        final double sinnok = Math.sin(xnodek);
        final double cosnok = Math.cos(xnodek);
        final double xmx = -sinnok * cosik;
        final double xmy = cosnok * cosik;
        final double ux = xmx * sinuk + cosnok * cosuk;
        final double uy = xmy * sinuk + sinnok * cosuk;
        final double uz = sinik * sinuk;
        final double vx = xmx * cosuk - cosnok * sinuk;
        final double vy = xmy * cosuk - sinnok * sinuk;
        final double vz = sinik * cosuk;
        position.setX(rk * ux);
        position.setY(rk * uy);
        position.setZ(rk * uz);
        velocity.setX(rdotk * ux + rfdotk * vx);
        velocity.setY(rdotk * uy + rfdotk * vy);
        velocity.setZ(rdotk * uz + rfdotk * vz);
        double phaseValue = xlt - xnode - omgadf + TWO_PI;
        if (phaseValue < 0.0) {
            phaseValue += TWO_PI;
        }
        satPos.setPhase(mod2PI(phaseValue));
    }
}
