package soccer.client;

import soccer.common.*;
import java.io.*;
import java.util.*;

public class Sensor {

    public static int END = 30;

    private int round;

    private World world;

    private Executor executor;

    private SoccerMaster soccerMaster;

    public Sensor(World world, Executor executor, SoccerMaster soccerMaster) {
        this.world = world;
        round = (int) (END * (60 / world.SIM_STEP_SECONDS));
        this.executor = executor;
        this.soccerMaster = soccerMaster;
    }

    public void sensing(Packet info) throws IOException {
        int min;
        int sec;
        Player player = null;
        Enumeration players = null;
        if (info.packetType == Packet.SEE) {
            world.see = (SeeData) info.data;
            world.me = world.see.player;
            world.status = world.see.status;
            world.ball = world.see.ball;
            world.leftTeam = world.see.leftTeam;
            world.rightTeam = world.see.rightTeam;
            sec = world.see.time / (int) (1 / world.SIM_STEP_SECONDS);
            min = sec / 60;
            sec = sec % 60;
            soccerMaster.timeJLabel.setText(min + ":" + sec);
            if (world.ball.controllerType != world.preController) {
                world.preController = world.ball.controllerType;
                if (world.ball.controllerType == 'f') soccerMaster.getSoundSystem().playClip("kick");
            }
            if (world.ball.controllerType == world.me.side && world.ball.controllerId == world.me.id) world.isBallKickable = true; else world.isBallKickable = false;
            Vector2d.subtract(world.ball.position, world.ballPosition, world.ballVelocity);
            world.ballPosition.setXY(world.ball.position);
            Vector2d.subtract(world.me.position, world.prePosition, world.myVelocity);
            world.prePosition.setXY(world.me.position);
            world.distance2Ball = world.see.player.position.distance(world.see.ball.position);
            world.direction2Ball = world.see.player.position.direction(world.see.ball.position);
            synchronized (world) {
                if (world.actionType == World.KICK || world.actionType == World.SHOOT || world.actionType == World.PASS) {
                    if (!world.isBallKickable) {
                        world.force = 0;
                        world.actionType = World.DRIVE;
                    }
                } else if (world.actionType == World.CHASE) {
                    if (world.isBallKickable) {
                        world.force = 0;
                        world.actionType = World.DRIVE;
                    }
                } else if (world.actionType == World.MOVE) {
                    double dist = world.destination.distance(world.me.position);
                    if (dist < 5) {
                        world.force = 0;
                        world.actionType = World.DRIVE;
                    }
                }
            }
            if (soccerMaster.isIn3D()) {
                soccerMaster.arena3D.repaint();
            } else {
                soccerMaster.arena2D.repaint();
            }
            int reactionTime = world.see.time - world.actionTime;
            if (reactionTime < 0) reactionTime += round;
            if (reactionTime >= World.INERTIA || world.isBallKickable) {
                executor.executing();
                world.actionTime = world.see.time;
            }
        } else if (info.packetType == Packet.HEAR) {
            world.message = (HearData) info.data;
            if (world.message.side == 'l') world.leftM = world.message; else if (world.message.side == 'r') world.rightM = world.message;
        } else if (info.packetType == Packet.REFEREE) {
            world.referee = (RefereeData) info.data;
            sec = world.referee.time / (int) (1 / world.SIM_STEP_SECONDS);
            min = sec / 60;
            sec = sec % 60;
            soccerMaster.periodJLabel.setText(RefereeData.periods[world.referee.period] + ":");
            soccerMaster.modeJLabel.setText(RefereeData.modes[world.referee.mode] + ":");
            soccerMaster.timeJLabel.setText(min + ":" + sec);
            soccerMaster.leftName.setText(world.referee.leftName);
            String scoreL = world.referee.score_L + " (" + world.referee.total_score_L + ")";
            soccerMaster.leftScore.setText(":" + scoreL);
            soccerMaster.rightName.setText(world.referee.rightName);
            String scoreR = world.referee.score_R + " (" + world.referee.total_score_R + ")";
            soccerMaster.rightScore.setText(":" + scoreR);
            if (world.referee.total_score_L > world.leftGoal) {
                world.leftGoal = world.referee.total_score_L;
                soccerMaster.getSoundSystem().playClip("applause");
            } else if (world.referee.total_score_R > world.rightGoal) {
                world.rightGoal = world.referee.total_score_R;
                soccerMaster.getSoundSystem().playClip("applause");
            } else if (world.referee.period != world.prePeriod) {
                soccerMaster.getSoundSystem().playClip("referee2");
                world.prePeriod = world.referee.period;
            } else if (world.referee.mode != world.preMode) {
                soccerMaster.getSoundSystem().playClip("referee1");
                world.preMode = world.referee.mode;
            }
        }
    }
}
