package jp.seraph.sample.matching;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MatchingMain {

    public static void main(String[] aArgs) {
        List<Doctor> tDoctors = new ArrayList<Doctor>();
        List<Hospital> tHospitals = new ArrayList<Hospital>();
        tDoctors.add(new Doctor("Dr. A"));
        tDoctors.add(new Doctor("Dr. B"));
        tDoctors.add(new Doctor("Dr. C"));
        tDoctors.add(new Doctor("Dr. D"));
        tDoctors.add(new Doctor("Dr. E"));
        tDoctors.add(new Doctor("Dr. F"));
        tDoctors.add(new Doctor("Dr. G"));
        tDoctors.add(new Doctor("Dr. H"));
        tDoctors.add(new Doctor("Dr. I"));
        tDoctors.add(new Doctor("Dr. J"));
        tDoctors.add(new Doctor("Dr. K"));
        tHospitals.add(new Hospital("H.1", 2));
        tHospitals.add(new Hospital("H.2", 5));
        tHospitals.add(new Hospital("H.3", 2));
        tHospitals.add(new Hospital("H.4", 3));
        init(tDoctors, tHospitals, new Random(111));
        System.out.println("before");
        ListUtil.printList(tHospitals);
        ListUtil.printList(tDoctors);
        new Matcher().match(tDoctors, tHospitals);
        testStable(tDoctors, tHospitals);
        System.out.println();
        System.out.println("after");
        ListUtil.printList(tHospitals);
        ListUtil.printList(tDoctors);
    }

    private static void init(List<Doctor> aDoctors, List<Hospital> aHospitals, Random aRandom) {
        int tDoctorCount = aDoctors.size();
        int tHospitalCount = aHospitals.size();
        for (Doctor tDoctor : aDoctors) {
            int tCount = aRandom.nextInt(tHospitalCount - 1) + 1;
            for (int i = 0; i < tCount; i++) {
                Hospital tHospital = aHospitals.get(aRandom.nextInt(tHospitalCount));
                if (!tDoctor.isBelongable(tHospital)) tDoctor.addBelongable(tHospital);
            }
        }
        for (Hospital tHospital : aHospitals) {
            int tCount = aRandom.nextInt(tDoctorCount - 1) + tHospital.getMax();
            for (int i = 0; i < tCount; i++) {
                Doctor tDoctor = aDoctors.get(aRandom.nextInt(tDoctorCount));
                if (!tHospital.isAcceptable(tDoctor)) tHospital.addAcceptable(tDoctor);
            }
        }
    }

    /**
     * このメソッドが正しいのかどうかはしらない。
     * マッチングの最中にデータを書き換えてしまっているので、どちらにしても気休め。
     *
     * @param aDoctors
     * @param aHospitals
     * @return
     */
    private static void testStable(List<Doctor> aDoctors, List<Hospital> aHospitals) {
        for (Doctor tDoctor : aDoctors) {
            if (tDoctor.hasBelongable() && !tDoctor.isBelonged()) throw new RuntimeException("所属していないドクターがいます");
            if (!tDoctor.isBelonged()) continue;
            List<Hospital> tMoreGood = tDoctor.getMoreGoodHospitals();
            for (Hospital tHospital : tMoreGood) {
                if (tHospital.isAcceptable(tDoctor)) {
                    if (tHospital.isMax()) {
                        List<Doctor> tAfters = tHospital.getAfterAndBelonged(tDoctor);
                        if (tAfters.size() > 0) throw new RuntimeException("より良い病院に配置できるはずのドクターがいます。");
                    } else {
                        throw new RuntimeException("より良い病院に配置できるはずのドクターがいます。");
                    }
                }
            }
        }
    }
}
