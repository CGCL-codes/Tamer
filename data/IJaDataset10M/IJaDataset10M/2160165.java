package edu.wpi.first.wpilibj.templates.subsystems;

import edu.wpi.first.wpilibj.CANJaguar;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.templates.Recoverable;
import edu.wpi.first.wpilibj.templates.RobotState;
import edu.wpi.first.wpilibj.templates.commands.manualDrive;

class CopyMasterToSlave implements Runnable {

    private static final CopyMasterToSlave copySingleton = new CopyMasterToSlave();

    public static CopyMasterToSlave getInstance() {
        return copySingleton;
    }

    private static boolean started = false;

    private static String driveName[] = { "", "" };

    private static byte syncGroup[] = { 0, 0 };

    private static CANJaguar master[] = { null, null };

    private static CANJaguar slave[][] = { { null, null }, { null, null } };

    private static volatile int numSets = 0;

    private CopyMasterToSlave() {
    }

    public void run() {
        double masterVoltage = 0;
        boolean skipWrite;
        while (true) {
            for (int drivetrain = 0; drivetrain < numSets; drivetrain++) {
                skipWrite = false;
                try {
                    if (null != master[drivetrain]) {
                        masterVoltage = master[drivetrain].getOutputVoltage();
                    } else {
                        skipWrite = true;
                    }
                } catch (edu.wpi.first.wpilibj.can.CANTimeoutException e) {
                    System.out.println("DriveSubsystem(" + driveName[drivetrain] + ") " + Timer.getFPGATimestamp() + ": CAN Timeout while reading voltage from master " + master[drivetrain].getDescription());
                    skipWrite = true;
                }
                if (!skipWrite) {
                    for (int idx = 0; idx < slave.length; idx++) {
                        try {
                            if (null != slave[drivetrain][idx]) {
                                slave[drivetrain][idx].setX(masterVoltage, syncGroup[drivetrain]);
                            }
                        } catch (edu.wpi.first.wpilibj.can.CANTimeoutException e) {
                            System.out.println("DriveSubsystem(" + driveName[drivetrain] + ") " + Timer.getFPGATimestamp() + ": CAN Timeout while copying voltage to slave  " + slave[drivetrain][idx].getDescription());
                        }
                        try {
                            CANJaguar.updateSyncGroup(syncGroup[drivetrain]);
                        } catch (edu.wpi.first.wpilibj.can.CANTimeoutException e) {
                            System.out.println("DriveSubsystem.drive(" + driveName[drivetrain] + ")" + Timer.getFPGATimestamp() + ": CAN Timeout while updating slave sync group ");
                        }
                    }
                }
            }
        }
    }

    public void addCopySet(String name, byte sync, int masterIdx, CANJaguar motors[]) {
        System.out.println("DriveSubsystem::CopyMasterToSlave - " + name);
        boolean goodMotors = true;
        System.out.println("motors.length = " + motors.length);
        if (goodMotors) {
            System.out.println("Index of Motor Sets to Copy to = " + numSets);
            driveName[numSets] = name;
            syncGroup[numSets] = sync;
            master[numSets] = motors[masterIdx];
            int slaveIdx = 0;
            for (int idx = 0; idx < motors.length; idx++) {
                if (idx != masterIdx) {
                    slave[numSets][slaveIdx] = motors[idx];
                    slaveIdx++;
                }
            }
            numSets++;
        } else {
            System.out.println("Got Bad Motors");
        }
        for (int idx = 0; idx < numSets; idx++) {
            System.out.println("driveName[" + idx + "] = " + driveName[idx]);
            System.out.println("syncGroup[" + idx + "] = " + syncGroup[idx]);
            if (null != master[idx]) {
                System.out.println("master[" + idx + "]    = " + master[idx].getDescription());
            } else {
                System.out.println("MASTER FAILURE " + driveName[idx]);
            }
            for (int jdx = 0; jdx < slave[idx].length; jdx++) {
                if (null != slave[idx][jdx]) {
                    System.out.println("slave[" + idx + "][" + jdx + "] = " + slave[idx][jdx].getDescription());
                } else {
                    System.out.println("SLAVE FAILURE " + slave[idx][jdx]);
                }
            }
        }
        System.out.println("DriveSubsystem::CopyMasterToSlave - Copyset Added for " + name);
    }

    public void beginCopying() {
        System.out.println("beginCopying");
        if (!started) {
            started = true;
            (new Thread(new CopyMasterToSlave())).start();
            System.out.println("DriveSubsystem::CopyMasterToSlave - Started");
        }
    }
}

public class DriveSubsystem extends Subsystem implements Recoverable {

    public static class DriveMode {

        public final int value;

        protected static final int m_kUninitialized = 0;

        protected static final int m_kSpeedControl = 1;

        protected static final int m_kDistanceControl = 2;

        public static final DriveMode kUninitialized = new DriveMode(m_kUninitialized);

        public static final DriveMode kSpeedControl = new DriveMode(m_kSpeedControl);

        public static final DriveMode kDistanceControl = new DriveMode(m_kDistanceControl);

        private DriveMode(int DriveMode) {
            this.value = DriveMode;
        }
    }

    private DriveMode m_driveMode = DriveMode.kUninitialized;

    private static final double m_kMaxMotorSpeed = 140;

    private static final double m_kMotorRevsPerMeter[] = { 50, 50 };

    private static final int m_maxMotors = 3;

    private static final int m_kMaxRetries = 5;

    private static final double m_kMaxVolts = 12;

    private static final int m_kRobotEncoderCountsPerRevolution[] = { 250, 360 };

    private int m_kEncoderCountsPerRevolution = m_kRobotEncoderCountsPerRevolution[RobotState.RobotID.kRook.value];

    private static byte m_instanceCount = 0;

    private byte m_syncGroup = 1;

    private static final int m_kMasterIdx = 0;

    private static final int m_kSlaveOneIdx = 1;

    private static final int m_kSlaveTwoIdx = 2;

    protected CANJaguar driveMotor[] = { null, null, null };

    private int m_motorID[] = { 1, 1, 1 };

    private boolean m_reverseDrive = false;

    private final double m_kPositionTolerance = 0.1;

    double commandRevolutions;

    double commandSpeed;

    String sideName = "NA";

    public DriveSubsystem(String side, final int master, final int slaveOne, final int slaveTwo) {
        RobotState robotState = RobotState.getInstance();
        sideName = side;
        m_motorID[m_kMasterIdx] = master;
        m_motorID[m_kSlaveOneIdx] = slaveOne;
        m_motorID[m_kSlaveTwoIdx] = slaveTwo;
        m_kEncoderCountsPerRevolution = m_kRobotEncoderCountsPerRevolution[robotState.getRobot().value];
        m_instanceCount++;
        m_syncGroup = (byte) (m_syncGroup << m_instanceCount);
        for (int idx = 0; idx < m_maxMotors; idx++) {
            int tries = 0;
            do {
                try {
                    driveMotor[idx] = new CANJaguar(m_motorID[idx]);
                } catch (edu.wpi.first.wpilibj.can.CANTimeoutException e) {
                    System.out.println("DriveSubsystem(" + sideName + "): CAN Timeout while creating motor interface for CAN ID " + m_motorID[idx]);
                    driveMotor[idx] = null;
                } catch (Exception all) {
                    System.out.println(all.toString());
                }
                tries++;
            } while ((null == driveMotor[idx]) && tries < m_kMaxRetries);
            if ((null == driveMotor[idx])) {
                System.out.println("DriveSubsystem(" + sideName + "): created motor " + m_motorID[idx]);
            }
        }
        for (int idx = m_kSlaveOneIdx; idx < m_maxMotors; idx++) {
            int tries = 0;
            boolean configured = false;
            do {
                try {
                    if (null != driveMotor[idx]) {
                        driveMotor[idx].changeControlMode(CANJaguar.ControlMode.kVoltage);
                        driveMotor[idx].enableControl();
                    }
                    configured = true;
                } catch (edu.wpi.first.wpilibj.can.CANTimeoutException e) {
                    System.out.println("DriveSubsystem(" + sideName + "): CAN Timeout while confguring slave motor CAN ID " + m_motorID[idx]);
                } catch (Exception all) {
                    System.out.println(all.toString());
                }
                tries++;
            } while (!configured && (tries < m_kMaxRetries));
            if (configured) {
                System.out.println("DriveSubsystem(" + sideName + "): Configured slave motor " + m_motorID[idx]);
            }
        }
        setSpeedMode();
        CopyMasterToSlave.getInstance().addCopySet(sideName, m_syncGroup, m_kMasterIdx, driveMotor);
        if (DriveMode.kUninitialized != m_driveMode) {
            CopyMasterToSlave.getInstance().beginCopying();
            System.out.println("DriveSubsystem(" + sideName + "): Started Copying ");
        }
        System.out.println("DriveSubsystem constructed");
    }

    public final void setSpeedMode() {
        if (null != driveMotor[m_kMasterIdx]) {
            double gain_Kp = 1.0;
            double gain_Ki = 0.0;
            double gain_Kd = 0.0;
            int tries = 0;
            boolean configured = false;
            do {
                try {
                    driveMotor[m_kMasterIdx].changeControlMode(CANJaguar.ControlMode.kSpeed);
                    driveMotor[m_kMasterIdx].setPositionReference(CANJaguar.PositionReference.kQuadEncoder);
                    driveMotor[m_kMasterIdx].configEncoderCodesPerRev(m_kEncoderCountsPerRevolution);
                    driveMotor[m_kMasterIdx].setPID(gain_Kp, gain_Ki, gain_Kd);
                    driveMotor[m_kMasterIdx].setX(0, m_syncGroup);
                    driveMotor[m_kMasterIdx].enableControl();
                    configured = true;
                } catch (edu.wpi.first.wpilibj.can.CANTimeoutException e) {
                    System.out.println("DriveSubsystem(" + sideName + "): CAN Timeout while configuring speed master CAN ID " + m_motorID[m_kMasterIdx]);
                } catch (Exception all) {
                    System.out.println(all.toString());
                }
                tries++;
            } while (!configured && (tries < m_kMaxRetries));
            if (configured) {
                m_driveMode = DriveMode.kSpeedControl;
                System.out.println("DriveSubsystem(" + sideName + "): Configured speed master motor " + m_motorID[m_kMasterIdx]);
            }
        }
    }

    public final void setDistanceMode() {
        if (null != driveMotor[m_kMasterIdx]) {
            double gain_Kp = 1.0;
            double gain_Ki = 0.0;
            double gain_Kd = 0.0;
            int tries = 0;
            boolean configured = false;
            do {
                try {
                    driveMotor[m_kMasterIdx].changeControlMode(CANJaguar.ControlMode.kPosition);
                    driveMotor[m_kMasterIdx].setPositionReference(CANJaguar.PositionReference.kQuadEncoder);
                    driveMotor[m_kMasterIdx].configEncoderCodesPerRev(m_kEncoderCountsPerRevolution);
                    driveMotor[m_kMasterIdx].setPID(gain_Kp, gain_Ki, gain_Kd);
                    driveMotor[m_kMasterIdx].setX(0, m_syncGroup);
                    driveMotor[m_kMasterIdx].enableControl(0);
                    configured = true;
                } catch (edu.wpi.first.wpilibj.can.CANTimeoutException e) {
                    System.out.println("DriveSubsystem(" + sideName + "): CAN Timeout while configuring distance master CAN ID " + m_motorID[m_kMasterIdx]);
                } catch (Exception all) {
                    System.out.println(all.toString());
                }
                tries++;
            } while (!configured && (tries < m_kMaxRetries));
            if (configured) {
                m_driveMode = DriveMode.kSpeedControl;
                System.out.println("DriveSubsystem(" + sideName + "): Configured distance master motor " + m_motorID[m_kMasterIdx]);
            }
        }
    }

    public void initDefaultCommand() {
        setDefaultCommand(new manualDrive());
    }

    public void drive(double speed) {
        commandSpeed = speed;
        SmartDashboard.putDouble(sideName + "DriveSpeed", speed);
        if (m_reverseDrive) {
            commandSpeed = -commandSpeed;
        }
        if (DriveMode.kSpeedControl == m_driveMode) {
            if (driveMotor[m_kMasterIdx] != null) {
                try {
                    driveMotor[m_kMasterIdx].setX(m_kMaxMotorSpeed * commandSpeed, m_syncGroup);
                } catch (edu.wpi.first.wpilibj.can.CANTimeoutException e) {
                    System.out.println("DriveSubsystem.drive(" + sideName + ")" + Timer.getFPGATimestamp() + ": CAN Timeout while setting master motor speed on motor " + m_motorID[m_kMasterIdx]);
                }
            }
        } else if ((DriveMode.kUninitialized == m_driveMode) || (driveMotor[m_kMasterIdx] == null)) {
            for (int idx = 0; idx < m_maxMotors; idx++) {
                try {
                    if (driveMotor[idx] != null) {
                        driveMotor[idx].setX(m_kMaxMotorSpeed * commandSpeed, m_syncGroup);
                        if (7 == m_motorID[idx]) {
                            System.out.println("@ " + m_motorID[idx] + " @ " + commandSpeed + "@ " + m_kMaxMotorSpeed * commandSpeed);
                        }
                    }
                } catch (edu.wpi.first.wpilibj.can.CANTimeoutException e) {
                    System.out.println("DriveSubsystem.drive(" + sideName + ")" + Timer.getFPGATimestamp() + ": CAN Timeout while setting motor speed on motor " + m_motorID[idx]);
                }
            }
        }
        try {
            CANJaguar.updateSyncGroup(m_syncGroup);
        } catch (edu.wpi.first.wpilibj.can.CANTimeoutException e) {
            System.out.println("DriveSubsystem.drive(" + sideName + ")" + Timer.getFPGATimestamp() + ": CAN Timeout while updating sync group " + m_syncGroup);
        }
    }

    public void move(double distance) {
        if (!m_reverseDrive) {
            distance = -distance;
        }
        SmartDashboard.putDouble(sideName + "DriveDistance", distance);
        commandRevolutions = distance / m_kMotorRevsPerMeter[RobotState.getInstance().getRobot().value];
        if (DriveMode.kDistanceControl == m_driveMode) {
            if (driveMotor[m_kMasterIdx] != null) {
                try {
                    driveMotor[m_kMasterIdx].setX(commandRevolutions, m_syncGroup);
                } catch (edu.wpi.first.wpilibj.can.CANTimeoutException e) {
                    System.out.println("DriveSubsystem.drive(" + sideName + ")" + Timer.getFPGATimestamp() + ": CAN Timeout while setting master motor distance on motor " + m_motorID[m_kMasterIdx]);
                }
            }
        }
        try {
            CANJaguar.updateSyncGroup(m_syncGroup);
        } catch (edu.wpi.first.wpilibj.can.CANTimeoutException e) {
            System.out.println("DriveSubsystem.drive(" + sideName + ")" + Timer.getFPGATimestamp() + ": CAN Timeout while updating sync group " + m_syncGroup);
        }
    }

    public void setReverse(boolean doReverse) {
        m_reverseDrive = doReverse;
    }

    public void setCoast(boolean doCoast) {
        for (int idx = 0; idx <= 2; idx++) {
            try {
                if (driveMotor[idx] != null) {
                    if (doCoast) {
                        driveMotor[idx].configNeutralMode(CANJaguar.NeutralMode.kCoast);
                    } else {
                        driveMotor[idx].configNeutralMode(CANJaguar.NeutralMode.kBrake);
                    }
                }
            } catch (edu.wpi.first.wpilibj.can.CANTimeoutException e) {
                System.out.println("DriveSubsystem.drive(" + sideName + "): CAN Timeout while setting coast/break on motor " + m_motorID[idx]);
            }
        }
    }

    public DriveMode getDriveMode() {
        return m_driveMode;
    }

    public boolean isMotionComplete() {
        if (DriveMode.kSpeedControl == m_driveMode) {
            return false;
        } else if (DriveMode.kSpeedControl == m_driveMode) {
            double position = 0;
            try {
                position = driveMotor[m_kMasterIdx].getPosition();
            } catch (edu.wpi.first.wpilibj.can.CANTimeoutException e) {
                System.out.println("DriveSubsystem.drive(" + sideName + "): CAN Timeout while reading distance traveled");
            }
            return (((position * (1 - m_kPositionTolerance)) <= commandRevolutions) && ((position * (1 + m_kPositionTolerance)) >= commandRevolutions));
        } else {
            return true;
        }
    }

    public void recover(boolean override) {
        boolean needsRecovery = false;
        for (int idx = m_kSlaveOneIdx; idx < m_maxMotors; idx++) {
            needsRecovery = false;
            try {
                if (null != driveMotor[idx]) {
                    needsRecovery = driveMotor[idx].getPowerCycled();
                }
            } catch (edu.wpi.first.wpilibj.can.CANTimeoutException e) {
                System.out.println("DriveSubsystem(" + sideName + "): CAN Timeout while checking for power loss " + m_motorID[idx]);
            }
            if (needsRecovery || override) {
                System.out.println("DriveSubsystem(" + sideName + ") " + Timer.getFPGATimestamp() + " Recovering " + m_motorID[idx]);
                int tries = 0;
                boolean configured = false;
                do {
                    try {
                        if (null != driveMotor[idx]) {
                            driveMotor[idx].changeControlMode(CANJaguar.ControlMode.kVoltage);
                            driveMotor[idx].enableControl();
                        }
                        configured = true;
                    } catch (edu.wpi.first.wpilibj.can.CANTimeoutException e) {
                        System.out.println("DriveSubsystem(" + sideName + "): CAN Timeout while reconfguring slave motor CAN ID " + m_motorID[idx]);
                    } catch (Exception all) {
                        System.out.println(all.toString());
                    }
                    tries++;
                } while (!configured && (tries < m_kMaxRetries));
                if (configured) {
                    System.out.println("DriveSubsystem(" + sideName + "): Reonfigured slave motor " + m_motorID[idx]);
                }
            }
        }
        needsRecovery = false;
        try {
            if (null != driveMotor[m_kMasterIdx]) {
                needsRecovery = driveMotor[m_kMasterIdx].getPowerCycled();
            }
        } catch (edu.wpi.first.wpilibj.can.CANTimeoutException e) {
            System.out.println("DriveSubsystem(" + sideName + "): CAN Timeout while checking for power loss " + m_motorID[m_kMasterIdx]);
        }
        if (needsRecovery || override) {
            System.out.println("DriveSubsystem(" + sideName + ") " + Timer.getFPGATimestamp() + " Recovering " + m_motorID[m_kMasterIdx]);
            if (DriveMode.kDistanceControl == m_driveMode) {
                setDistanceMode();
            } else {
                setSpeedMode();
            }
        }
    }
}
