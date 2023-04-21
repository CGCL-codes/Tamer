package gov.nasa.jpf.complexcoverage.test;

import gov.nasa.jpf.complexcoverage.Debug;
import gov.nasa.jpf.jvm.Verify;

public class ASW_REQ2 {

    private PowerOnConditions6 PowerOnConditions_1086_class_member0 = new PowerOnConditions6();

    private PowerSignalError7 PowerSignalError_1087_class_member1 = new PowerSignalError7();

    private DOICmd8 DOICmd_1085_class_member2 = new DOICmd8();

    private AltimeterError9 AltimeterError_1083_class_member3 = new AltimeterError9();

    private AlarmDetermination10 AlarmDetermination_1084_class_member4 = new AlarmDetermination10();

    public void Main2(double Altitude_Input_2, boolean AltitudeOK_Input_3, boolean DOIOn_Input_4, boolean Inhibit_Input_5, boolean Reset_Input_6, double Threshold_Input_7, boolean[] AlarmOn_8, boolean[] DOICommand_9) {
        boolean sig_0[] = new boolean[1];
        boolean sig_1[] = new boolean[1];
        boolean sig_2[] = new boolean[1];
        PowerOnConditions_1086_class_member0.Main12(AltitudeOK_Input_3, Altitude_Input_2, Threshold_Input_7, DOIOn_Input_4, sig_1);
        PowerSignalError_1087_class_member1.Main13(DOIOn_Input_4, sig_1[0], sig_2);
        DOICmd_1085_class_member2.Main14(sig_1[0], Inhibit_Input_5, DOICommand_9);
        AltimeterError_1083_class_member3.Main15(AltitudeOK_Input_3, sig_0);
        AlarmDetermination_1084_class_member4.Main16(sig_0[0], sig_2[0], Inhibit_Input_5, Reset_Input_6, AlarmOn_8);
    }

    public void Init5() {
        AltimeterError_1083_class_member3.Init17();
        AlarmDetermination_1084_class_member4.Init18();
        DOICmd_1085_class_member2.Init19();
        PowerOnConditions_1086_class_member0.Init20();
        PowerSignalError_1087_class_member1.Init21();
    }
}
