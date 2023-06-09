package cx.fbn.nevernote.filters;

import java.util.List;
import com.trolltech.qt.sql.QSqlDatabase;
import com.trolltech.qt.sql.QSqlQuery;
import cx.fbn.nevernote.Global;

public class WordFilter {

    private final List<String> wordList;

    QSqlDatabase db;

    public WordFilter(QSqlDatabase d, List<String> list) {
        wordList = list;
        db = d;
        QSqlQuery query = new QSqlQuery(db);
        QSqlQuery insert = new QSqlQuery(db);
        if (wordList == null) return;
        if (wordList.size() == 0) return;
        query.exec("create temporary table guidList (guid text)");
        query.clear();
        query.exec("delete from guidList");
        query.clear();
        query.prepare("Select guid from words where word like :word and weight>=:weight");
        insert.prepare("insert into guidList (guid) values (:guid)");
        for (int i = 0; i < wordList.size(); i++) {
            query.bindValue(":word", wordList.get(i) + "%");
            query.bindValue(":weight", Global.getRecognitionWeight());
            if (!query.exec()) {
                Global.logger.log(Global.logger.LOW, query.lastError().toString());
            } else {
                while (query.next()) {
                    insert.bindValue(":guid", query.value(0).toString());
                    insert.exec();
                }
            }
        }
        query.clear();
        insert.clear();
        insert.finish();
        query.finish();
    }

    public boolean contains(String guid) {
        if (wordList.size() == 0) return true;
        QSqlQuery query = new QSqlQuery(db);
        query.prepare("select count(guid) from guidList where guid=:guid");
        query.bindValue("guid", guid);
        query.exec();
        while (query.next()) {
            Integer count = new Integer(query.value(0).toString());
            if (count >= wordList.size()) {
                query.clear();
                return true;
            }
        }
        query.clear();
        return false;
    }
}
