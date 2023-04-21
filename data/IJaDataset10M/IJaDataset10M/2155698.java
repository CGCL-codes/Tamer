package org.fudaa.fudaa.tr;

import gnu.trove.TDoubleArrayList;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import org.fudaa.ctulu.CtuluAnalyze;
import org.fudaa.ctulu.CtuluUIDefault;
import org.fudaa.dodico.ef.operation.EfTrajectoireActivity;
import org.fudaa.dodico.ef.operation.EfTrajectoireParameters;
import org.fudaa.dodico.ef.operation.EfTrajectoireResultBuilder;
import org.fudaa.dodico.fichiers.FileFormatSoftware;
import org.fudaa.dodico.h2d.type.H2dVariableType;
import org.fudaa.fudaa.tr.post.TrPostSource;
import org.fudaa.fudaa.tr.post.TrPostSourceBuilder;
import org.fudaa.fudaa.tr.post.dialogSpec.TrPostTrajectoireTaskModel;
import org.fudaa.fudaa.tr.post.profile.MvProfileCoteTester;
import com.vividsolutions.jts.geom.Coordinate;

/**
 * @author deniger
 */
public class TestTrajectoire {

    public static void main(String[] args) {
        if (args == null || args.length == 0) {
            System.err.println("No file set");
            System.exit(1);
        }
        File f = new File(args[0]);
        if (!f.exists()) {
            System.err.println("File " + f + " not found");
            System.exit(1);
        }
        CtuluUIDefault ui = new CtuluUIDefault();
        TrPostSource src = TrPostSourceBuilder.activeSourceAction(f, ui, FileFormatSoftware.TELEMAC_IS.name, null, null);
        if (src == null) {
            System.err.println("Problem in reading file");
            System.exit(1);
        }
        double[] initTimeSteps = src.getTime().getInitTimeSteps();
        EfTrajectoireParameters param = new EfTrajectoireParameters();
        param.vx = H2dVariableType.VITESSE_U;
        param.vy = H2dVariableType.VITESSE_V;
        param.dureeIntegration_ = -18000;
        param.finesse_ = 1;
        param.isLigneDeCourant = false;
        param.firstTimeStepIdx_ = 71;
        param.firstTimeStep_ = initTimeSteps[param.firstTimeStepIdx_];
        param.nbPointsInitiaux_ = 15;
        Coordinate deb = new Coordinate(477286, 241715, 0);
        Coordinate end = new Coordinate(477316, 241654, 0);
        param.segment_ = Arrays.asList(deb, end);
        param.points_ = TrPostTrajectoireTaskModel.getPointsFromSegment(deb, end, param.nbPointsInitiaux_);
        CtuluAnalyze analyze = new CtuluAnalyze();
        EfTrajectoireActivity act = new EfTrajectoireActivity(src, initTimeSteps);
        act.setTester(new MvProfileCoteTester());
        long t = System.nanoTime();
        List<EfTrajectoireResultBuilder> computeLigneCourant = act.computeLigneCourant(param, null, analyze);
        System.err.println((System.nanoTime() - t) * 1E-9);
        t = System.nanoTime();
        analyze.printResume();
    }

    private static void compareTimes(TDoubleArrayList times, TDoubleArrayList times2) {
        if (times.size() == times2.size()) {
            for (int i = 0; i < times.size(); i++) {
                double c = times.get(i);
                double c2 = times2.get(i);
                if (Math.abs(c - c2) > 1E-3) {
                    System.err.println("temps pas egales pour i=" + i);
                }
            }
        } else {
            System.err.println("times non egale");
        }
    }

    private static void compareCoords(List<Coordinate> coords, List<Coordinate> coords2) {
        if (coords.size() == coords2.size()) {
            for (int i = 0; i < coords.size(); i++) {
                Coordinate c = coords.get(i);
                Coordinate c2 = coords2.get(i);
                if (c.distance(c2) > 1E-3) {
                    System.err.println("coordonn�es pas egales pour i=" + i);
                }
            }
        } else {
            System.err.println("taille non egale");
        }
    }
}
