package de.evaluationtool.format;

import java.io.File;
import de.evaluationtool.Evaluation;

public interface EvaluationFormat {

    Evaluation readEvaluation(File f);
}
