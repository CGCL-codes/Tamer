package net.sf.josceleton.core.api.entity.body;

import net.sf.josceleton.core.api.entity.body.BodyParts.Ankle;
import net.sf.josceleton.core.api.entity.body.BodyParts.Ankles;
import net.sf.josceleton.core.api.entity.body.BodyParts.Elbow;
import net.sf.josceleton.core.api.entity.body.BodyParts.Elbows;
import net.sf.josceleton.core.api.entity.body.BodyParts.Feet;
import net.sf.josceleton.core.api.entity.body.BodyParts.Foot;
import net.sf.josceleton.core.api.entity.body.BodyParts.Hand;
import net.sf.josceleton.core.api.entity.body.BodyParts.Hands;
import net.sf.josceleton.core.api.entity.body.BodyParts.Head;
import net.sf.josceleton.core.api.entity.body.BodyParts.Hip;
import net.sf.josceleton.core.api.entity.body.BodyParts.Hips;
import net.sf.josceleton.core.api.entity.body.BodyParts.Knee;
import net.sf.josceleton.core.api.entity.body.BodyParts.Knees;
import net.sf.josceleton.core.api.entity.body.BodyParts.LeftAnkle;
import net.sf.josceleton.core.api.entity.body.BodyParts.LeftElbow;
import net.sf.josceleton.core.api.entity.body.BodyParts.LeftFoot;
import net.sf.josceleton.core.api.entity.body.BodyParts.LeftHand;
import net.sf.josceleton.core.api.entity.body.BodyParts.LeftHip;
import net.sf.josceleton.core.api.entity.body.BodyParts.LeftKnee;
import net.sf.josceleton.core.api.entity.body.BodyParts.LeftShoulder;
import net.sf.josceleton.core.api.entity.body.BodyParts.Neck;
import net.sf.josceleton.core.api.entity.body.BodyParts.RightAnkle;
import net.sf.josceleton.core.api.entity.body.BodyParts.RightElbow;
import net.sf.josceleton.core.api.entity.body.BodyParts.RightFoot;
import net.sf.josceleton.core.api.entity.body.BodyParts.RightHand;
import net.sf.josceleton.core.api.entity.body.BodyParts.RightHip;
import net.sf.josceleton.core.api.entity.body.BodyParts.RightKnee;
import net.sf.josceleton.core.api.entity.body.BodyParts.RightShoulder;
import net.sf.josceleton.core.api.entity.body.BodyParts.Shoulder;
import net.sf.josceleton.core.api.entity.body.BodyParts.Shoulders;
import net.sf.josceleton.core.api.entity.body.BodyParts.Torso;

final class BodyPartImplProvider {

    private BodyPartImplProvider() {
    }

    static class HeadImpl extends DefaultBodyPart implements Head {

        HeadImpl() {
            super("Head", "head");
        }
    }

    static class NeckImpl extends DefaultBodyPart implements Neck {

        NeckImpl() {
            super("Neck", "neck");
        }
    }

    static class TorsoImpl extends DefaultBodyPart implements Torso {

        TorsoImpl() {
            super("Torso", "torso");
        }
    }

    private static class LeftShoulderImpl extends DefaultBodyPart implements LeftShoulder {

        LeftShoulderImpl() {
            super("Left Shoulder", "l_shoulder");
        }
    }

    private static class RightShoulderImpl extends DefaultBodyPart implements RightShoulder {

        RightShoulderImpl() {
            super("Right Shoulder", "r_shoulder");
        }
    }

    static class ShouldersImpl extends DefaultSymetricBodyPart<Shoulder, LeftShoulder, RightShoulder> implements Shoulders {

        ShouldersImpl() {
            super(new LeftShoulderImpl(), new RightShoulderImpl());
        }
    }

    private static class LeftElbowImpl extends DefaultBodyPart implements LeftElbow {

        LeftElbowImpl() {
            super("Left Elbow", "l_elbow");
        }
    }

    private static class RightElbowImpl extends DefaultBodyPart implements RightElbow {

        RightElbowImpl() {
            super("Right Elbow", "r_elbow");
        }
    }

    static class ElbowsImpl extends DefaultSymetricBodyPart<Elbow, LeftElbow, RightElbow> implements Elbows {

        ElbowsImpl() {
            super(new LeftElbowImpl(), new RightElbowImpl());
        }
    }

    private static class LeftHandImpl extends DefaultBodyPart implements LeftHand {

        LeftHandImpl() {
            super("Left Hand", "l_hand");
        }
    }

    private static class RightHandImpl extends DefaultBodyPart implements RightHand {

        RightHandImpl() {
            super("Right Hand", "r_hand");
        }
    }

    static class HandsImpl extends DefaultSymetricBodyPart<Hand, LeftHand, RightHand> implements Hands {

        HandsImpl() {
            super(new LeftHandImpl(), new RightHandImpl());
        }
    }

    private static class LeftHipImpl extends DefaultBodyPart implements LeftHip {

        LeftHipImpl() {
            super("Left Hip", "l_hip");
        }
    }

    private static class RightHipImpl extends DefaultBodyPart implements RightHip {

        RightHipImpl() {
            super("Right Hip", "r_hip");
        }
    }

    static class HipsImpl extends DefaultSymetricBodyPart<Hip, LeftHip, RightHip> implements Hips {

        HipsImpl() {
            super(new LeftHipImpl(), new RightHipImpl());
        }
    }

    private static class LeftKneeImpl extends DefaultBodyPart implements LeftKnee {

        LeftKneeImpl() {
            super("Left Knee", "l_knee");
        }
    }

    private static class RightKneeImpl extends DefaultBodyPart implements RightKnee {

        RightKneeImpl() {
            super("Right Knee", "r_knee");
        }
    }

    static class KneesImpl extends DefaultSymetricBodyPart<Knee, LeftKnee, RightKnee> implements Knees {

        KneesImpl() {
            super(new LeftKneeImpl(), new RightKneeImpl());
        }
    }

    private static class LeftAnkleImpl extends DefaultBodyPart implements LeftAnkle {

        LeftAnkleImpl() {
            super("Left Ankle", "l_ankle");
        }
    }

    private static class RightAnkleImpl extends DefaultBodyPart implements RightAnkle {

        RightAnkleImpl() {
            super("Right Ankle", "r_ankle");
        }
    }

    static class AnklesImpl extends DefaultSymetricBodyPart<Ankle, LeftAnkle, RightAnkle> implements Ankles {

        AnklesImpl() {
            super(new LeftAnkleImpl(), new RightAnkleImpl());
        }
    }

    private static class LeftFootImpl extends DefaultBodyPart implements LeftFoot {

        LeftFootImpl() {
            super("Left Foot", "l_foot");
        }
    }

    private static class RightFootImpl extends DefaultBodyPart implements RightFoot {

        RightFootImpl() {
            super("Right Foot", "r_foot");
        }
    }

    static class FeetImpl extends DefaultSymetricBodyPart<Foot, LeftFoot, RightFoot> implements Feet {

        FeetImpl() {
            super(new LeftFootImpl(), new RightFootImpl());
        }
    }

    interface LeftPart<X> {
    }

    interface RightPart<X> {
    }

    interface SymetricBodyPart<X, L extends LeftPart<X>, R extends RightPart<X>> {

        L LEFT();

        R RIGHT();
    }

    private static class DefaultBodyPart implements BodyPart {

        private final String label;

        private final String osceletonId;

        DefaultBodyPart(final String label, final String osceletonId) {
            this.label = label;
            this.osceletonId = osceletonId;
        }

        @Override
        public final String getLabel() {
            return this.label;
        }

        @Override
        public final String getOsceletonId() {
            return this.osceletonId;
        }
    }

    private static class DefaultSymetricBodyPart<X, L extends LeftPart<X>, R extends RightPart<X>> implements SymetricBodyPart<X, L, R> {

        private final L leftPart;

        private final R rightPart;

        DefaultSymetricBodyPart(final L leftPart, final R rightPart) {
            this.leftPart = leftPart;
            this.rightPart = rightPart;
        }

        @Override
        public final L LEFT() {
            return this.leftPart;
        }

        @Override
        public final R RIGHT() {
            return this.rightPart;
        }
    }
}
