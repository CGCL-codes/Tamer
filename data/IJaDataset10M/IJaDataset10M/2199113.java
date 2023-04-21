package org.fpdev.apps.rtemaster.analysis;

import org.fpdev.core.algo.IntegerInterval;
import org.fpdev.core.basenet.Path;
import org.fpdev.core.transit.CalendarService;
import org.fpdev.core.transit.SubRoute;
import org.fpdev.core.transit.TransitPath;
import org.fpdev.util.FPUtil;

public class VisualizedRun implements IntegerInterval {

    private SubRoute sub_;

    private int run_;

    private int start_;

    private int end_;

    private boolean stopMode_;

    private double dwellTime_, accDecTime_;

    private double pathLenW_;

    private double[] subPathLensW_;

    private double[] timeSpans_;

    public VisualizedRun(SubRoute sub, CalendarService service, int run) {
        super();
        sub_ = sub;
        run_ = run;
        start_ = sub_.getTime(service, run_, 0);
        end_ = sub_.getTime(service, run_, sub.stopCount() - 1);
        stopMode_ = (sub.getPath().getType() != TransitPath.Type.BUS);
        dwellTime_ = 20;
        accDecTime_ = 15;
        pathLenW_ = sub_.getPath().lengthFt(true);
        subPathLensW_ = new double[sub_.getPath().getTimePointCount() - 1];
        timeSpans_ = new double[sub_.getPath().getTimePointCount() - 1];
        for (int tptIndex = 0; tptIndex < sub_.getPath().getTimePointCount() - 1; tptIndex++) {
            Path subpath = sub_.getPath().subPath(sub_.getPath().getStop(sub_.getTimePointStopIndex(tptIndex)).getOutPLI(), sub_.getPath().getStop(sub_.getTimePointStopIndex(tptIndex + 1)).getInPLI());
            subPathLensW_[tptIndex] = subpath.lengthFt(true);
            timeSpans_[tptIndex] = sub_.getTime(service, run, sub_.getTimePointStopIndex(tptIndex + 1)) - sub_.getTime(service, run, sub_.getTimePointStopIndex(tptIndex));
        }
    }

    public int getLower() {
        return start_;
    }

    public int getUpper() {
        return end_;
    }

    public boolean contains(int x) {
        return start_ <= x && x <= end_;
    }

    public SubRoute getSubRoute() {
        return sub_;
    }

    public int getRun() {
        return run_;
    }

    public double getLocationAtTime(CalendarService service, int run, int stime) {
        TransitPath path = sub_.getPath();
        int startTime = sub_.getTime(service, run, 0), endTime = sub_.getTime(service, run, sub_.stopCount() - 1);
        if (stime < startTime || stime > endTime) {
            return -1;
        }
        if (stime == startTime) return 0;
        double r = 0;
        int tptIndex;
        for (tptIndex = 0; tptIndex < path.getTimePointCount() - 1; tptIndex++) {
            if (sub_.getTime(service, run, sub_.getTimePointStopIndex(tptIndex)) < stime && stime <= sub_.getTime(service, run, sub_.getTimePointStopIndex(tptIndex + 1))) break; else r += subPathLensW_[tptIndex] / pathLenW_;
        }
        double timePastTPt = stime - sub_.getTime(service, run, sub_.getTimePointStopIndex(tptIndex));
        double i = stopMode_ ? stopModeDist(tptIndex, timePastTPt) : timePastTPt / timeSpans_[tptIndex];
        r += (i * subPathLensW_[tptIndex]) / pathLenW_;
        return r;
    }

    private double stopModeDist(int segmentIndex, double t) {
        double span = timeSpans_[segmentIndex];
        if (t <= dwellTime_ / 2) return 0;
        if (t >= span - dwellTime_ / 2) return 1;
        double totalArea = span - accDecTime_ - dwellTime_;
        if (t >= dwellTime_ / 2 + accDecTime_ && t <= span - dwellTime_ / 2 - accDecTime_) {
            double top = ((t - dwellTime_ / 2 - accDecTime_) + accDecTime_ / 2);
            double r = top / totalArea;
            return r;
        }
        if (t <= dwellTime_ / 2 + accDecTime_) {
            double r = sinInt(((t - dwellTime_ / 2) / accDecTime_) * Math.PI / 2) * (accDecTime_ / 2) / totalArea;
            return r;
        }
        double ci = cosInt(((t - span + accDecTime_ + dwellTime_ / 2) / accDecTime_) * Math.PI / 2);
        double r = (ci * (accDecTime_ / 2) + accDecTime_ / 2 + (span - 2 * accDecTime_ - dwellTime_)) / totalArea;
        return r;
    }

    private double sinInt(double upper) {
        return -Math.cos(upper) + 1;
    }

    private double cosInt(double upper) {
        return Math.sin(upper);
    }

    @Override
    public String toString() {
        return "RunInterval for " + sub_.getMasterID() + ", r=" + run_ + ", start=" + FPUtil.dtime(start_) + ", end=" + FPUtil.dtime(end_);
    }
}
