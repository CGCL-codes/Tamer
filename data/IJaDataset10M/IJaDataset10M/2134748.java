package ie.dkit.java3Demulation.drawer;

import ie.dkit.java3Demulation.objects.*;
import java.awt.Graphics;

public class EllipseDrawer extends CircleDrawer {

    public EllipseDrawer(Graphics graphics, int xMiddle, int ymiddle) {
        super(graphics, xMiddle, ymiddle);
    }

    public void draw(Ellipse eclipse) {
        drawEclipseAlgorithm(eclipse.getCenter_A(), eclipse.getCenter_B(), eclipse.getRadius(), eclipse.getCenter());
    }

    private void drawEclipseAlgorithm(Point cA, Point cB, double rad, Point center) {
        if (cA == cB) {
            drawCircleAlgorithm(center, rad);
        } else {
            double dist = (Math.sqrt(Math.pow(cA.getX() - cB.getX(), 2) + Math.pow(cA.getY() - cB.getY(), 2)));
            double xcA = 0;
            double xcB = 0;
            double ycA = 0;
            double ycB = 0;
            double ang = 0;
            System.out.println(cA.getX());
            System.out.println(cA.getY());
            System.out.println(cB.getX());
            System.out.println(cB.getY());
            if ((cA.getX() > cB.getX()) && (cA.getY() > cB.getY())) {
                xcA = cB.getX();
                xcB = cA.getX();
                ycA = cB.getY();
                ycB = cA.getY();
                ang = Math.atan((xcB - xcA) / (ycB - ycA)) + Math.PI;
                ang = ang % Math.PI;
            } else if ((cA.getX() > cB.getX()) && (cA.getY() < cB.getY())) {
                xcA = cA.getX();
                xcB = cB.getX();
                ycA = cA.getY();
                ycB = cB.getY();
                ang = Math.atan((xcB - xcA) / (ycB - ycA));
                ang = ang % Math.PI;
            } else if ((cA.getX() < cB.getX()) && (cA.getY() > cB.getY())) {
                xcA = cB.getX();
                xcB = cA.getX();
                ycA = cB.getY();
                ycB = cA.getY();
                ang = Math.atan((xcB - xcA) / (ycB - ycA));
                ang = ang % Math.PI;
            } else if ((cA.getX() < cB.getX()) && (cA.getY() < cB.getY())) {
                xcA = cA.getX();
                xcB = cB.getX();
                ycA = cA.getY();
                ycB = cB.getY();
                ang = Math.atan((xcB - xcA) / (ycB - ycA)) + Math.PI;
                ang = ang % Math.PI;
            } else {
                xcA = cA.getX();
                xcB = cB.getX();
                ycA = cA.getY();
                ycB = cB.getY();
                ang = Math.atan((xcB - xcA) / (ycB - ycA)) + Math.PI;
                ang = ang % Math.PI;
            }
            if (ang < 0) {
                ang = ang + Math.PI;
                ang = ang % Math.PI;
            }
            System.out.println(dist);
            double xpoint = -((rad * xcB - dist * xcA - rad * xcA) / (dist));
            System.out.println(xpoint);
            double xpoint2 = (dist * xcB + rad * xcB - rad * xcA) / (dist);
            System.out.println(xpoint2);
            double ypoint = -((rad * ycB - dist * ycA - rad * ycA) / (dist));
            System.out.println(ypoint);
            double ypoint2 = (dist * ycB + rad * ycB - rad * ycA) / (dist);
            System.out.println(ypoint2);
            double dist2 = dist + rad * 2;
            double y = ypoint;
            double x = xpoint;
            double xz = xpoint;
            writePixel((int) x, (int) y);
            for (y = ypoint + 0.0000000001; y <= ypoint2; y++) {
                double distA = (Math.sqrt(Math.pow(x - xcA, 2) + Math.pow(y - ycA, 2)));
                double distB = (Math.sqrt(Math.pow(x - xcB, 2) + Math.pow(y - ycB, 2)));
                System.out.println(dist2);
                System.out.println(distA);
                System.out.println(distB);
                System.out.println(ang);
                while (distA + distB > dist2) {
                    if (ang >= Math.PI / 2) {
                        x--;
                    } else {
                        x++;
                    }
                    distA = (Math.sqrt(Math.pow(x - xcA, 2) + Math.pow(y - ycA, 2)));
                    distB = (Math.sqrt(Math.pow(x - xcB, 2) + Math.pow(y - ycB, 2)));
                }
                while (distA + distB < dist2) {
                    if (ang >= Math.PI / 2) {
                        x++;
                    } else {
                        x--;
                    }
                    distA = (Math.sqrt(Math.pow(x - xcA, 2) + Math.pow(y - ycA, 2)));
                    distB = (Math.sqrt(Math.pow(x - xcB, 2) + Math.pow(y - ycB, 2)));
                }
                double alkashi = (Math.pow(distB, 2) - Math.pow(dist, 2) - Math.pow(distA, 2)) / (2 * dist * distA);
                if (alkashi > 1) {
                    alkashi = 1;
                }
                System.out.println("YOP");
                double angle = 2 * Math.acos(alkashi);
                double xt = x - xcA;
                double yt = y - ycA;
                double xr = 0;
                double yr = 0;
                if (ang > Math.PI / 2) {
                    xr = xt * Math.cos(-angle) - yt * Math.sin(-angle);
                    yr = xt * Math.sin(-angle) + yt * Math.cos(-angle);
                } else {
                    xr = xt * Math.cos(angle) - yt * Math.sin(angle);
                    yr = xt * Math.sin(angle) + yt * Math.cos(angle);
                }
                xt = xr + xcA;
                yt = yr + ycA;
                writePixel((int) x, (int) y);
                writePixel((int) xt, (int) yt);
                xz = x;
            }
            y = ypoint2;
            if (ang > Math.PI / 2) {
                for (x = xpoint2; x <= xz; x++) {
                    double distA = (Math.sqrt(Math.pow(x - xcA, 2) + Math.pow(y - ycA, 2)));
                    double distB = (Math.sqrt(Math.pow(x - xcB, 2) + Math.pow(y - ycB, 2)));
                    while (distA + distB > dist2) {
                        y--;
                        distA = (Math.sqrt(Math.pow(x - xcA, 2) + Math.pow(y - ycA, 2)));
                        distB = (Math.sqrt(Math.pow(x - xcB, 2) + Math.pow(y - ycB, 2)));
                    }
                    while (distA + distB < dist2) {
                        y++;
                        distA = (Math.sqrt(Math.pow(x - xcA, 2) + Math.pow(y - ycA, 2)));
                        distB = (Math.sqrt(Math.pow(x - xcB, 2) + Math.pow(y - ycB, 2)));
                    }
                    double alkashi = (Math.pow(distB, 2) - Math.pow(dist, 2) - Math.pow(distA, 2)) / (2 * dist * distA);
                    if (alkashi > 1) {
                        alkashi = 1;
                    }
                    double angle = 2 * Math.acos(alkashi);
                    double xt = x - xcA;
                    double yt = y - ycA;
                    double xr = 0;
                    double yr = 0;
                    if (ang > Math.PI / 2) {
                        xr = xt * Math.cos(-angle) - yt * Math.sin(-angle);
                        yr = xt * Math.sin(-angle) + yt * Math.cos(-angle);
                    } else {
                        xr = xt * Math.cos(angle) - yt * Math.sin(angle);
                        yr = xt * Math.sin(angle) + yt * Math.cos(angle);
                    }
                    xt = xr + xcA;
                    yt = yr + ycA;
                    writePixel((int) x, (int) y);
                    writePixel((int) xt, (int) yt);
                }
            } else {
                for (x = xpoint2; x >= xz; x--) {
                    double distA = (Math.sqrt(Math.pow(x - xcA, 2) + Math.pow(y - ycA, 2)));
                    double distB = (Math.sqrt(Math.pow(x - xcB, 2) + Math.pow(y - ycB, 2)));
                    while (distA + distB > dist2) {
                        y--;
                        distA = (Math.sqrt(Math.pow(x - xcA, 2) + Math.pow(y - ycA, 2)));
                        distB = (Math.sqrt(Math.pow(x - xcB, 2) + Math.pow(y - ycB, 2)));
                    }
                    while (distA + distB < dist2) {
                        y++;
                        distA = (Math.sqrt(Math.pow(x - xcA, 2) + Math.pow(y - ycA, 2)));
                        distB = (Math.sqrt(Math.pow(x - xcB, 2) + Math.pow(y - ycB, 2)));
                    }
                    double alkashi = (Math.pow(distB, 2) - Math.pow(dist, 2) - Math.pow(distA, 2)) / (2 * dist * distA);
                    if (alkashi > 1) {
                        alkashi = 1;
                    }
                    double angle = 2 * Math.acos(alkashi);
                    double xt = x - xcA;
                    double yt = y - ycA;
                    double xr = 0;
                    double yr = 0;
                    if (ang > Math.PI / 2) {
                        xr = xt * Math.cos(-angle) - yt * Math.sin(-angle);
                        yr = xt * Math.sin(-angle) + yt * Math.cos(-angle);
                    } else {
                        xr = xt * Math.cos(angle) - yt * Math.sin(angle);
                        yr = xt * Math.sin(angle) + yt * Math.cos(angle);
                    }
                    xt = xr + xcA;
                    yt = yr + ycA;
                    writePixel((int) x, (int) y);
                    writePixel((int) xt, (int) yt);
                }
            }
        }
    }
}
