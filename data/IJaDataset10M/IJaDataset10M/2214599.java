package uk.ac.shef.wit.trex.experiments.relation;

import uk.ac.shef.wit.commons.UtilCollections;
import uk.ac.shef.wit.runes.Runes;
import uk.ac.shef.wit.runes.exceptions.RunesExceptionCannotHandle;
import uk.ac.shef.wit.runes.exceptions.RunesExceptionNoSuchContent;
import uk.ac.shef.wit.runes.exceptions.RunesExceptionNoSuchStructure;
import uk.ac.shef.wit.runes.exceptions.RunesExceptionRuneExecution;
import uk.ac.shef.wit.runes.rune.Rune;
import uk.ac.shef.wit.runes.runestone.Content;
import uk.ac.shef.wit.runes.runestone.Runestone;
import uk.ac.shef.wit.runes.runestone.Structure;
import uk.ac.shef.wit.runes.runestone.StructureAndContent;
import uk.ac.shef.wit.text.util.UtilTags;
import java.text.MessageFormat;
import java.util.*;

public class RuneAIMedAnnotationsExtractor implements Rune {

    private static final double VERSION = 0.1;

    static {
        Runes.registerRune(RuneAIMedAnnotationsExtractor.class, MessageFormat.format("t-rex AIMed dataset annotations extractor v{0}", VERSION));
    }

    public Set<String> analyseRequired(final Runestone stone) {
        return UtilCollections.add(new HashSet<String>(), "$_text", "$_has_text", "$_has_untagged_$", "dataset_marker_AIMed");
    }

    public Set<String> analyseProvided(final Runestone stone) {
        return UtilCollections.add(new HashSet<String>(), "entity_annotation", "start_offset_in_$", "start_offset_in_untagged_$", "end_offset_in_$", "end_offset_in_untagged_$", "entity_class", "entity_annotation_from_$", "entity_annotation_from_untagged_$", "entity_annotation_has_start_offset_in_$", "entity_annotation_has_start_offset_in_untagged_$", "entity_annotation_has_end_offset_in_$", "entity_annotation_has_end_offset_in_untagged_$", "entity_annotation_has_entity_class", "next_entity_annotation_in_$", "previous_entity_annotation_in_$", "$_has_entity_annotation", "untagged_$_has_entity_annotation", "relation_annotation", "relation_class", "relation_annotation_has_subject", "relation_annotation_has_object", "relation_annotation_has_relation_class");
    }

    public void carve(final Runestone stone) throws RunesExceptionNoSuchStructure, RunesExceptionNoSuchContent, RunesExceptionCannotHandle, RunesExceptionRuneExecution {
        final Content<String> contents = stone.getContent("$_text");
        final Structure hasUntagged = stone.getStructure("$_has_untagged_$");
        final StructureAndContent<Integer> startOffsets = stone.getStructureAndContent("start_offset_in_$");
        final StructureAndContent<Integer> ustartOffsets = stone.getStructureAndContent("start_offset_in_untagged_$");
        final StructureAndContent<Integer> endOffsets = stone.getStructureAndContent("end_offset_in_$");
        final StructureAndContent<Integer> uendOffsets = stone.getStructureAndContent("end_offset_in_untagged_$");
        final StructureAndContent<String> entityClasses = stone.getStructureAndContent("entity_class");
        final Structure entityAnnotations = stone.getStructure("entity_annotation", "entity_annotation_from_$", "entity_annotation_has_start_offset_in_$", "entity_annotation_has_end_offset_in_$", "entity_annotation_has_entity_class");
        final Structure ufrom = stone.getStructure("entity_annotation_from_untagged_$");
        final Structure ustartOffset = stone.getStructure("entity_annotation_has_start_offset_in_untagged_$");
        final Structure uendOffset = stone.getStructure("entity_annotation_has_end_offset_in_untagged_$");
        final Structure nextEntityAnnotation = stone.getStructure("next_entity_annotation_in_$");
        final Structure previousEntityAnnotation = stone.getStructure("previous_entity_annotation_in_$");
        final Structure hasEntityAnnotation = stone.getStructure("$_has_entity_annotation");
        final Structure uhasEntityAnnotation = stone.getStructure("untagged_$_has_entity_annotation");
        final StructureAndContent<String> relationClasses = stone.getStructureAndContent("relation_class");
        final Structure relationAnnotations = stone.getStructure("relation_annotation", "relation_annotation_has_subject", "relation_annotation_has_object", "relation_annotation_has_relation_class");
        final int relationClassId = relationClasses.encode("AIMed_relation");
        for (final int[] hc : stone.getStructure("$_has_text")) {
            final int sentenceId = hc[0];
            final int untaggedId = hasUntagged.follow(hc[0]);
            final String content = contents.retrieve(hc[1]);
            final List<Markup> markup = new LinkedList<Markup>();
            int start = -1, end, displacement = 0;
            while (-1 != (start = content.indexOf('<', start + 1)) && -1 != (end = content.indexOf('>', start + 1))) {
                int nextStart;
                while ((nextStart = content.indexOf('<', start + 1)) < end && -1 != nextStart) start = nextStart;
                final String tag = content.substring(start, end + 1);
                markup.add(new Markup(tag, start + 1, start - displacement + 1));
                displacement += tag.length();
            }
            final Stack<Markup> openTagsPending = new Stack<Markup>();
            final Stack<Set<Integer>> entitiesInRelation = new Stack<Set<Integer>>();
            final Map<Integer, Set<Integer>> participants = new HashMap<Integer, Set<Integer>>();
            int previousEAId = 0;
            for (final Markup current : markup) {
                final String tag = current.getTag();
                if (UtilTags.isOpenTag(tag)) {
                    openTagsPending.push(current);
                    if (!UtilTags.isSimpleTag(tag)) entitiesInRelation.push(new HashSet<Integer>());
                } else if (!openTagsPending.isEmpty()) {
                    final Markup lastOpen = openTagsPending.pop();
                    final String openTag = lastOpen.getTag();
                    if (UtilTags.areTagPair(tag, openTag)) {
                        if (UtilTags.isSimpleTag(openTag)) {
                            final int startAtId = startOffsets.encode(lastOpen.getTaggedPos());
                            final int endAtId = endOffsets.encode(current.getTaggedPos() + current.getTag().length());
                            final int entityClassId = entityClasses.encode(tag);
                            final int id = entityAnnotations.encode(sentenceId, startAtId, endAtId, entityClassId);
                            ufrom.inscribe(id, untaggedId);
                            ustartOffset.inscribe(id, ustartOffsets.encode(lastOpen.getUntaggedPos()));
                            uendOffset.inscribe(id, uendOffsets.encode(current.getUntaggedPos()));
                            nextEntityAnnotation.inscribe(previousEAId, id);
                            previousEntityAnnotation.inscribe(id, previousEAId);
                            hasEntityAnnotation.inscribe(sentenceId, id);
                            uhasEntityAnnotation.inscribe(untaggedId, id);
                            previousEAId = id;
                            for (final Set<Integer> ids : entitiesInRelation) ids.add(id);
                        } else {
                            final StringTokenizer tokenizer = new StringTokenizer(UtilTags.getRoot(openTag));
                            final boolean isSubject = "p1".equals(tokenizer.nextToken().trim());
                            final Integer reference = new Integer(tokenizer.nextToken().trim().substring(5));
                            final Set<Integer> other = participants.get(reference);
                            if (other == null) participants.put(reference, entitiesInRelation.pop()); else {
                                final Set<Integer> subjectIds = isSubject ? entitiesInRelation.pop() : other;
                                final Set<Integer> objectIds = isSubject ? other : entitiesInRelation.pop();
                                for (final Integer subjectId : subjectIds) for (final Integer objectId : objectIds) relationAnnotations.inscribe(subjectId, objectId, relationClassId);
                                participants.remove(reference);
                            }
                        }
                    } else throw new RunesExceptionRuneExecution("open/close tag mismatch around here: " + content.substring(Math.max(current.getTaggedPos() - 20, 0), Math.min(current.getTaggedPos() + 20, content.length())), this);
                } else throw new RunesExceptionRuneExecution("missing close tag around here: " + content.substring(Math.max(current.getTaggedPos() - 20, 0), Math.min(current.getTaggedPos() + 20, content.length())), this);
            }
        }
    }

    public String toString() {
        return "AIMed dataset annotations extractor";
    }

    private class Markup {

        private final String _tag;

        private final int _taggedPos;

        private final int _untaggedPos;

        private Markup(final String tag, final int taggedPos, final int untaggedPos) {
            _tag = tag;
            _taggedPos = taggedPos;
            _untaggedPos = untaggedPos;
        }

        public String getTag() {
            return _tag;
        }

        public int getTaggedPos() {
            return _taggedPos;
        }

        public int getUntaggedPos() {
            return _untaggedPos;
        }
    }
}
