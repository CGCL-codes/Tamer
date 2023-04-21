package bioevent.coreference.attributecalculators;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import bioevent.core.CorefExample;
import bioevent.core.ICoreferenceAttributeCalculator;
import bioevent.core.WordsPhrase;
import bioevent.db.entities.ArtifactTable;
import bioevent.db.entities.PhraseTable;
import bioevent.edgedetection.StanfordDependenciesTree;

public class Anaphora_Subject implements ICoreferenceAttributeCalculator {

    @Override
    public int addAttributeValues(CorefExample exampleToProcess) throws SQLException {
        int isSubject = 0;
        WordsPhrase anaphora = exampleToProcess.anaphora;
        String filepath = exampleToProcess.associatedFilePath;
        ResultSet phrase = PhraseTable.getByID(anaphora.phrase_id);
        int toArtifactID = phrase.getInt("to_artifact_id");
        int sentenceID = phrase.getInt("sentence_artifact_id");
        ResultSet artifact_record = ArtifactTable.getByID(toArtifactID);
        String word = artifact_record.getString("text_content");
        StanfordDependenciesTree parses = new StanfordDependenciesTree(filepath);
        List<String> dependencies = new ArrayList<String>();
        try {
            dependencies = parses.getDependencyTypes(word, sentenceID);
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (int i = 0; i < dependencies.size(); i++) {
            if (dependencies.get(i).matches("[cnx].subj(pass)*")) {
                isSubject++;
            }
        }
        return isSubject;
    }
}
