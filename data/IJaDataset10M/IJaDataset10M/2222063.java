package prgechecsimpla;

import java.util.ArrayList;
import prgechecsia.IA;
import prgechecsmodel.IGenerator;

public abstract class GeneratorImplA implements IGenerator {

    public GeneratorImplA() {
    }

    public static ArrayList<IA> getmIA() {
        return mIA;
    }
}
