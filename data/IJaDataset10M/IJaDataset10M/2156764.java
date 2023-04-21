package org.arpenteur.photogrammetry.optimization;

import java.util.ArrayList;
import java.util.StringTokenizer;
import org.arpenteur.common.manager.ManagerAction;
import org.arpenteur.common.math.geometry.GeometricalItem;
import org.arpenteur.common.math.geometry.point.IPoint3D;
import org.arpenteur.common.math.geometry.transformation.Transformation3D;
import org.arpenteur.common.math.matrices.mException.MatrixException;
import org.arpenteur.common.misc.io.AFileReader;
import org.arpenteur.common.misc.logging.Logging;
import org.arpenteur.photogrammetry.orientation.constraint.FixedPoint;
import org.arpenteur.photogrammetry.orientation.constraint.IFixedPoint;

public abstract class AComputeAdaptation3D extends AComputeAdaptation {

    public static final int LAMBDA = 0x08;

    public static final int OMEGA = 0x10;

    public static final int PHI = 0x20;

    public static final int KAPPA = 0x40;

    private int valeurImposee;

    private double phi, omega, kappa, lambda, xu, yu, zu;

    private Transformation3D impose;

    protected AComputeAdaptation3D(ArrayList<IPoint3D> observation) {
        super(observation);
    }

    protected AComputeAdaptation3D(ArrayList<IPoint3D> observation, ArrayList<IPoint3D> ref) {
        super(observation, ref);
    }

    public void init() {
        super.init();
        setNbUnk(7);
        setNbCondition(0);
        impose = new Transformation3D();
    }

    public void affect(Object o) {
        super.affect(o);
        if (o instanceof AComputeAdaptation3D) {
            AComputeAdaptation3D source = (AComputeAdaptation3D) o;
            phi(source.phi());
            omega(source.omega());
            kappa(source.kappa());
            lambda(source.lambda());
            xu(source.xu());
            yu(source.yu());
            zu(source.zu());
            impose(new Transformation3D(source.impose()), source.getValeurImposee());
        }
    }

    protected double xu() {
        return xu;
    }

    protected double yu() {
        return yu;
    }

    protected double zu() {
        return zu;
    }

    protected double lambda() {
        return lambda;
    }

    protected double omega() {
        return omega;
    }

    protected double phi() {
        return phi;
    }

    protected double kappa() {
        return kappa;
    }

    protected void xu(double a) {
        xu = a;
    }

    protected void yu(double a) {
        yu = a;
    }

    protected void zu(double a) {
        zu = a;
    }

    protected void lambda(double a) {
        lambda = a;
    }

    protected void omega(double a) {
        omega = a;
    }

    protected void phi(double a) {
        phi = a;
    }

    protected void kappa(double a) {
        kappa = a;
    }

    /**
	 * Surcharge de ACalculOptimisation.calculerEmq() Calcul de EMQ et residus
	 * des points
	 */
    public boolean computeRMS() {
        boolean result = false;
        if ((isOptimizationPossible()) && (getComputeStatusNumber() >= AComputeOptimization.VAL_APP_OK)) {
            result = true;
            computeRMSResiduals();
            verifierPointsHorsTolerance();
        }
        return result;
    }

    protected double computeRMSResiduals() {
        setAverage(0);
        setRMS(0);
        setMaxRes(0.0);
        setMinRes(0.0);
        int n = 0;
        for (int i = 0; i < nbReference(); i++) {
            IPoint3D pref = getReferenceAt(i);
            if ((pref != null) && pref.isValid()) {
                IPoint3D pobs = getHomologueMesure(i);
                if ((pobs != null) && (pobs.isValid())) {
                    IPoint3D res = residu(pobs, pref);
                    setResidual(pobs, res);
                    double ecart_pt = res.getNorm();
                    if ((pref.isActive()) && (pobs.isActive())) {
                        if (coordonneeImposee(pref, IFixedPoint.X_FIXED)) {
                            n++;
                            setMaxRes((getMaxRes() > res.getX()) ? getMaxRes() : res.getX());
                            setMinRes((getMinRes() < res.getX()) ? getMinRes() : res.getX());
                            setRMS(getRMS() + res.getX() * res.getX());
                            setAverage(getAverage() + res.getX());
                        }
                        if (coordonneeImposee(pref, IFixedPoint.Y_FIXED)) {
                            n++;
                            setMaxRes((getMaxRes() > res.getY()) ? getMaxRes() : res.getY());
                            setMinRes((getMinRes() < res.getY()) ? getMinRes() : res.getY());
                            setRMS(getRMS() + res.getY() * res.getY());
                            setAverage(getAverage() + res.getY());
                        }
                        if (coordonneeImposee(pref, IFixedPoint.Z_FIXED)) {
                            n++;
                            setMaxRes((getMaxRes() > res.getZ()) ? getMaxRes() : res.getZ());
                            setMinRes((getMinRes() < res.getZ()) ? getMinRes() : res.getZ());
                            setRMS(getRMS() + res.getZ() * res.getZ());
                            setAverage(getAverage() + res.getZ());
                        }
                    }
                }
            }
        }
        setAverage(getAverage() / n);
        setRMS(Math.sqrt(getRMS() / (n - 7)));
        return getRMS();
    }

    public double getGap(IPoint3D p) {
        double result = -1;
        if (getMeasures() != null) {
            int rang = getMeasures().indexOf(p);
            IPoint3D ref = getHomologueReference(p);
            if ((rang >= 0) && (ref != null)) {
                IPoint3D residu = residu(p, ref);
                result = residu.getNorm();
            }
        }
        return result;
    }

    protected abstract void setResidual(IPoint3D obs, IPoint3D residu);

    private IPoint3D residu(IPoint3D obs, IPoint3D ref) {
        return ref.minus(transform(obs));
    }

    protected abstract void verifierPointsHorsTolerance();

    /**
	 * Compte les equations en jeu dans la structure 'references' = une equation
	 * par coordonnee active d'un point de reference qui possede un homologue
	 * mesure actif
	 */
    public int getNumberOfEquations() {
        int nb = 0;
        for (int i = 0; i < nbReference(); i++) {
            IPoint3D pRef = getReferenceAt(i);
            if (pointActifValide(pRef)) {
                IPoint3D pMes = getHomologueMesure(i);
                if (pointActifValide(pMes)) {
                    if (coordonneeImposee(pRef, IFixedPoint.X_FIXED)) {
                        nb++;
                    }
                    if (coordonneeImposee(pRef, IFixedPoint.Y_FIXED)) {
                        nb++;
                    }
                    if (coordonneeImposee(pRef, IFixedPoint.Z_FIXED)) {
                        nb++;
                    }
                }
            }
        }
        return nb;
    }

    /**
	 * Affiche les points participants au calcul
	 */
    public void showPoints_inCalcul() {
        for (int i = 0; i < nbReference(); i++) {
            IPoint3D ref = getReferenceAt(i);
            if (pointActifValide(ref)) {
                IPoint3D obs = getHomologueMesure(i);
                if (pointActifValide(obs)) {
                    Logging.log.println(" ref : " + ref);
                    Logging.log.println(" obs : " + obs);
                }
            }
        }
    }

    public IPoint3D getHomologueMesure(int i) {
        if (getMeasures() != null && i < getMeasures().size()) {
            return getMeasures().get(i);
        }
        return null;
    }

    public IPoint3D getHomologueMesure(IPoint3D pm) {
        if ((getMeasures() != null) && (pm != null)) {
            return ManagerAction.getPtById(getMeasures(), pm.getName(), pm.getIdn());
        }
        return null;
    }

    public IPoint3D getHomologueReference(int i) {
        if (getReference() != null && i < getReference().size()) {
            return getReference().get(i);
        }
        return null;
    }

    public IPoint3D getHomologueReference(IPoint3D pm) {
        if ((getReference() != null) && (pm != null)) {
            return ManagerAction.getPtById(getReference(), pm.getName(), pm.getIdn());
        }
        return null;
    }

    protected boolean checkDataResult(GeometricalItem item) {
        return (item == null) || (item instanceof Transformation3D);
    }

    protected boolean coordonneeImposee(IPoint3D p, int coord) {
        if (p != null) {
            if (p instanceof IFixedPoint) {
                IFixedPoint pf = (IFixedPoint) p;
                return pf.isCoordFixed(coord);
            } else {
                return true;
            }
        }
        return false;
    }

    protected String defaultName() {
        return "Adaptation3D";
    }

    public Transformation3D getTransformation() {
        return (Transformation3D) getDataResult();
    }

    protected Transformation3D impose() {
        return impose;
    }

    protected void impose(Transformation3D imp, int vImpose) {
        if ((imp != null) && (vImpose != NONE)) {
            setValeurImposee(vImpose);
            impose = imp;
        }
    }

    public void imposerAngles(int lesquels, int convAngulaire, double aomega, double aphi, double akappa) {
        if (lesquels != NONE) {
            setValeurImposee(getValeurImposee() | lesquels);
            double angles[] = null;
            try {
                angles = impose.getAnglesRotation(convAngulaire);
            } catch (MatrixException e) {
                e.printStackTrace();
            }
            if (isValeurImposee(OMEGA & lesquels)) {
                angles[0] = aomega;
            }
            if (isValeurImposee(PHI & lesquels)) {
                angles[1] = aphi;
            }
            if (isValeurImposee(KAPPA & lesquels)) {
                angles[2] = akappa;
            }
            try {
                impose = new Transformation3D(convAngulaire, impose.getTranslation(), impose.getLambda(), angles[0], angles[1], angles[2]);
            } catch (MatrixException e) {
                e.printStackTrace();
            }
        }
    }

    public void imposerLambda(double aLambda) {
        setValeurImposee(getValeurImposee() | LAMBDA);
        impose = new Transformation3D(impose.getRotationMatrix(), impose.getTranslation(), aLambda);
    }

    public void imposerTranslation(int lesquels, IPoint3D translation) {
        if (lesquels != NONE) {
            setValeurImposee(getValeurImposee() | lesquels);
            IPoint3D tr = impose.getTranslation();
            if (isValeurImposee(IFixedPoint.X_FIXED & lesquels)) {
                tr.setX(translation.getX());
            }
            if (isValeurImposee(IFixedPoint.Y_FIXED & lesquels)) {
                tr.setY(translation.getY());
            }
            if (isValeurImposee(IFixedPoint.Z_FIXED & lesquels)) {
                tr.setZ(translation.getZ());
            }
            impose = new Transformation3D(impose.getRotationMatrix(), tr, impose.getLambda());
        }
    }

    public int getValeurImposee() {
        return valeurImposee;
    }

    public boolean isValeurImposee(int coord) {
        int vImp = getValeurImposee();
        if (vImp != NONE) {
            switch(coord) {
                case IFixedPoint.X_FIXED:
                    return (vImp & IFixedPoint.X_FIXED) != 0;
                case IFixedPoint.Y_FIXED:
                    return (vImp & IFixedPoint.Y_FIXED) != 0;
                case IFixedPoint.Z_FIXED:
                    return (vImp & IFixedPoint.Z_FIXED) != 0;
                case LAMBDA:
                    return (vImp & LAMBDA) != 0;
                case OMEGA:
                    return (vImp & OMEGA) != 0;
                case PHI:
                    return (vImp & PHI) != 0;
                case KAPPA:
                    return (vImp & KAPPA) != 0;
                default:
                    return (vImp & coord) != 0;
            }
        }
        return false;
    }

    public int getNbEqu() {
        return getNumberOfEquations();
    }

    public boolean isOptimizationPossible() {
        if (getComputeStatusNumber() == AComputeOptimization.VAL_APP_OK) {
            return true;
        }
        if (getNbMeasures() == 0 || nbReference() == 0) {
            return false;
        }
        boolean result = false;
        int nb = 0;
        for (int i = 0; i < nbReference(); i++) {
            IPoint3D pRef = getReferenceAt(i);
            if (i < getNbMeasures()) {
                IPoint3D pObs = getHomologueMesure(i);
                if (pointActifValide(pRef) && pointActifValide(pObs)) {
                    if (pointReferenceComplet(pRef)) {
                        nb++;
                    }
                }
                if (nb >= 3) {
                    result = true;
                    break;
                }
            }
        }
        return result;
    }

    protected boolean pointActifValide(IPoint3D p) {
        return (p != null) && (p.isActive()) && (p.isValid());
    }

    protected boolean pointReferenceComplet(IPoint3D p) {
        return (pointActifValide(p) && (coordonneeImposee(p, IFixedPoint.X_FIXED) && coordonneeImposee(p, IFixedPoint.Y_FIXED) && coordonneeImposee(p, IFixedPoint.Z_FIXED)));
    }

    protected abstract void setOptimizationResult();

    public void setValeurImposee(int valImposees) {
        valeurImposee = valImposees;
    }

    protected Transformation3D transformation() {
        return (Transformation3D) getDataResult();
    }

    public IPoint3D transform(IPoint3D p) {
        return transformation().getTransformedPoint(p);
    }

    public IPoint3D transformInverse(IPoint3D p) {
        return transformation().getTransformedPointInverse(p);
    }

    public boolean valeursImposeesPossible() {
        return false;
    }

    protected static IPoint3D readPoint3D(String line, boolean isRef) {
        IPoint3D result = null;
        StringTokenizer token = new StringTokenizer(line);
        String[] tokens = new String[token.countTokens()];
        int count = 0;
        while (token.hasMoreTokens()) {
            tokens[count] = token.nextToken();
            tokens[count].trim();
            count++;
        }
        int iAlpha = 0;
        if (count > 3) {
            String chiffres = new String("0123456789");
            char c = tokens[0].charAt(0);
            if (chiffres.indexOf(c) >= 0) {
                iAlpha = -1;
            }
            double x = 0;
            double y = 0;
            double z = 0;
            String alpha = " ";
            int num = -1;
            try {
                if (iAlpha >= 0) {
                    alpha = new String(tokens[0]);
                }
                num = Integer.parseInt(tokens[iAlpha + 1]);
                x = Double.valueOf(tokens[iAlpha + 2]).doubleValue();
                y = Double.valueOf(tokens[iAlpha + 3]).doubleValue();
                z = Double.valueOf(tokens[iAlpha + 4]).doubleValue();
                if (isRef) {
                    int vImp = IFixedPoint.COORD_NONE;
                    for (int i = iAlpha + 5; i <= iAlpha + 7; i++) {
                        if ((i < tokens.length) && ((tokens[i].equals("X")) || (tokens[i].equals("Y")) || (tokens[i].equals("Z")))) {
                            if (tokens[i].equals("X")) {
                                vImp |= IFixedPoint.X_FIXED;
                            }
                            if (tokens[i].equals("Y")) {
                                vImp |= IFixedPoint.Y_FIXED;
                            }
                            if (tokens[i].equals("Z")) {
                                vImp |= IFixedPoint.Z_FIXED;
                            }
                        }
                    }
                    if (vImp == IFixedPoint.COORD_NONE) {
                        vImp = IFixedPoint.XYZ_FIXED;
                    }
                    result = new FixedPoint(x, y, z, vImp);
                } else {
                    result = newPoint3D(x, y, z);
                }
                result.setIdn(num);
                result.setName(alpha);
            } catch (Throwable t) {
                return null;
            }
        }
        return result;
    }
}
