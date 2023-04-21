package com.brygade.GradeMailer;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;
import com.csvreader.CsvReader;

public class MessageGenerator {

    private static final String BASE_MESSAGE = "Sorry, I forgot to attach my comments, so I had to send this one out twice...\n\n" + "Assignment 1 Score for %s\n" + "==========\n" + "Part 1: %s/10\n" + "Part 2: %s/30\n" + "Part 3: %s/60\n" + "----------\n" + "Total: %s/100\n\n" + "Comments:\n";

    private CsvReader reader;

    public MessageGenerator(String csvFilePath) throws FileNotFoundException {
        reader = new CsvReader(csvFilePath);
    }

    public List<MessageBean> generateMessages() throws IOException {
        List<MessageBean> result = new ArrayList<MessageBean>(29);
        reader.readHeaders();
        while (reader.readRecord()) {
            MessageBean msg = new MessageBean();
            msg.setTo(reader.get("alias") + "@calpoly.edu");
            msg.setFrom("bestrada@calpoly.edu");
            msg.setSubject("[CSC232] Assignment 1 Score for: " + reader.get("name") + " with my comments");
            Formatter formatter = new Formatter();
            String base = new String(BASE_MESSAGE);
            String content = formatter.format(base, reader.get("name"), reader.get("part1"), reader.get("part2"), reader.get("part3"), reader.get("total")).toString();
            content = content + (reader.get("notes").replaceAll(":", "\n"));
            msg.setContent(content);
            result.add(msg);
        }
        return result;
    }
}
