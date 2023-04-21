package org.ximtec.igesture.util.additionsandroid;

import org.ximtec.igesture.util.additions3d.AccelerationSample;
import org.ximtec.igesture.util.additions3d.Accelerations;
import org.ximtec.igesture.util.additions3d.Note3D;
import org.ximtec.igesture.util.additions3d.Point3D;

public class AndroidTools {

    public static Note3D accelerationsToTraces(Accelerations acc) {
        Note3D gesture = new Note3D();
        double xVelocity = 0;
        double yVelocity = 0;
        double zVelocity = 0;
        double xPosition = 0;
        double yPosition = 0;
        double zPosition = 0;
        long tOld = 0;
        long tNew = 0;
        double a;
        Point3D point = new Point3D();
        for (int i = 0; i < acc.numberOfSamples(); i++) {
            AccelerationSample sample = acc.getSample(i);
            tNew = sample.getTimeStamp();
            if (i == 0) {
                tOld = tNew;
            }
            double t = (tNew - tOld) * 0.001;
            tOld = tNew;
            a = sample.getXAcceleration();
            xPosition += (a * t);
            a = sample.getYAcceleration();
            yPosition += (a * t);
            a = sample.getZAcceleration();
            zPosition += (a * t);
            point.set(xPosition, yPosition, zPosition);
            point.setTimeStamp(sample.getTimeStamp());
            System.out.println(point);
            gesture.add(point);
            point = new Point3D();
            sample = new AccelerationSample();
        }
        gesture.setAccelerations(acc);
        return gesture;
    }
}
