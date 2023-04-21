package org.saosis.core.controllers.features.builders;

import java.util.ArrayList;
import org.saosis.core.controllers.FeedbackController;
import org.saosis.core.controllers.FormatUtility;
import org.saosis.core.controllers.StringUtility;
import org.saosis.core.controllers.features.FeatureBuilder;
import org.saosis.core.controllers.features.FeatureFormatUtility;
import org.saosis.core.models.DatabaseCrossReference;
import org.saosis.core.models.Entry;
import org.saosis.core.models.Feature;
import org.saosis.core.models.FeatureInference;
import org.saosis.core.models.FeatureQualifier;
import org.saosis.core.models.Location;
import org.saosis.core.models.SAOSISException;
import org.saosis.core.models.Vocabularies;
import org.saosis.core.models.features.OriginOfTransferFeature;

/**
 * An interface for building and converting OriginOfTransferFeature objects.
 * 
 * @author Daniel Allen Prust (danprust@yahoo.com)
 * 
 */
public class OriginOfTransferFeatureBuilder extends FeatureBuilder implements IFeatureBuilder {

    public Feature buildFeature(FeedbackController feedbackController, String name, ArrayList<FeatureQualifier> qualifiers) {
        OriginOfTransferFeature feature = new OriginOfTransferFeature();
        feature.setName(name);
        for (FeatureQualifier qualifier : qualifiers) {
            try {
                if ("allele".equals(qualifier.getName())) {
                    feature.getAllele().add(StringUtility.replaceNewlines(StringUtility.unQuote(qualifier.getValue()), " "));
                } else if ("bound_moiety".equals(qualifier.getName())) {
                    feature.getBoundMoieties().add(StringUtility.replaceNewlines(StringUtility.unQuote(qualifier.getValue()), " "));
                } else if ("citation".equals(qualifier.getName())) {
                    FormatUtility.errorIfNotEmpty(feedbackController, "citation", feature.getCitation());
                    int value = StringUtility.parseBracketedInt(qualifier.getValue());
                    feature.setCitation(value);
                } else if ("db_xref".equals(qualifier.getName())) {
                    DatabaseCrossReference value = new DatabaseCrossReference();
                    FeatureFormatUtility.fromString(value, qualifier.getValue());
                    feature.getDatabaseCrossReferences().add(value);
                } else if ("direction".equals(qualifier.getName())) {
                    FormatUtility.errorIfNotEmpty(feedbackController, "direction", feature.getDirection());
                    Vocabularies.ReplicationDirections value = Vocabularies.ReplicationDirections.fromINSDCString(qualifier.getValue());
                    feature.setDirection(value);
                } else if ("experiment".equals(qualifier.getName())) {
                    FormatUtility.errorIfNotEmpty(feedbackController, "experiment", feature.getExperiment());
                    String value = StringUtility.replaceNewlines(StringUtility.unQuote(qualifier.getValue()), " ");
                    feature.setExperiment(value);
                } else if ("gene".equals(qualifier.getName())) {
                    FormatUtility.errorIfNotEmpty(feedbackController, "gene", feature.getGene());
                    String value = StringUtility.replaceNewlines(StringUtility.unQuote(qualifier.getValue()), " ");
                    feature.setGene(value);
                } else if ("gene_synonym".equals(qualifier.getName())) {
                    feature.getGeneSynonyms().add(StringUtility.replaceNewlines(StringUtility.unQuote(qualifier.getValue()), " "));
                } else if ("inference".equals(qualifier.getName())) {
                    FormatUtility.errorIfNotEmpty(feedbackController, "inference", feature.getInference());
                    FeatureInference value = new FeatureInference();
                    FeatureFormatUtility.fromString(value, qualifier.getValue());
                    feature.setInference(value);
                } else if ("label".equals(qualifier.getName())) {
                    FormatUtility.errorIfNotEmpty(feedbackController, "label", feature.getLabel());
                    String value = StringUtility.replaceNewlines(StringUtility.unQuote(qualifier.getValue()), " ");
                    feature.setLabel(value);
                } else if ("locus_tag".equals(qualifier.getName())) {
                    FormatUtility.errorIfNotEmpty(feedbackController, "locus_tag", feature.getLocusTag());
                    String value = StringUtility.replaceNewlines(StringUtility.unQuote(qualifier.getValue()), " ");
                    feature.setLocusTag(value);
                } else if ("map".equals(qualifier.getName())) {
                    FormatUtility.errorIfNotEmpty(feedbackController, "map", feature.getMap());
                    String value = StringUtility.replaceNewlines(StringUtility.unQuote(qualifier.getValue()), " ");
                    feature.setMap(value);
                } else if ("note".equals(qualifier.getName())) {
                    FormatUtility.errorIfNotEmpty(feedbackController, "note", feature.getNotes());
                    String value = StringUtility.replaceNewlines(StringUtility.unQuote(qualifier.getValue()), " ");
                    feature.setNotes(value);
                } else if ("old_locus_tag".equals(qualifier.getName())) {
                    feature.getOldLocusTags().add(StringUtility.replaceNewlines(StringUtility.unQuote(qualifier.getValue()), " "));
                } else if ("rpt_family".equals(qualifier.getName())) {
                    FormatUtility.errorIfNotEmpty(feedbackController, "rpt_family", feature.getRepeatFamily());
                    String value = StringUtility.replaceNewlines(StringUtility.unQuote(qualifier.getValue()), " ");
                    feature.setRepeatFamily(value);
                } else if ("rpt_type".equals(qualifier.getName())) {
                    FormatUtility.errorIfNotEmpty(feedbackController, "rpt_type", feature.getRepeatType());
                    Vocabularies.RepeatTypes value = Vocabularies.RepeatTypes.fromINSDCString(qualifier.getValue());
                    feature.setRepeatType(value);
                } else if ("rpt_unit_range".equals(qualifier.getName())) {
                    FormatUtility.errorIfNotEmpty(feedbackController, "rpt_unit_range", feature.getRepeatUnitRange());
                    Location value = new Location();
                    FeatureFormatUtility.fromString(value, qualifier.getValue());
                    feature.setRepeatUnitRange(value);
                } else if ("rpt_unit_seq".equals(qualifier.getName())) {
                    FormatUtility.errorIfNotEmpty(feedbackController, "rpt_unit_seq", feature.getRepeatUnitSequence());
                    String value = StringUtility.replaceNewlines(StringUtility.unQuote(qualifier.getValue()), " ");
                    feature.setRepeatUnitSequence(value);
                } else if ("standard_name".equals(qualifier.getName())) {
                    FormatUtility.errorIfNotEmpty(feedbackController, "standard_name", feature.getStandardName());
                    String value = StringUtility.replaceNewlines(StringUtility.unQuote(qualifier.getValue()), " ");
                    feature.setStandardName(value);
                } else unexpectedQualifierError(feedbackController, name, qualifier.getName());
            } catch (SAOSISException e) {
                feedbackController.addError(e);
            }
        }
        return feature;
    }

    public ArrayList<FeatureQualifier> buildQualifiers(Entry entry, Feature generalFeature) {
        OriginOfTransferFeature feature = (OriginOfTransferFeature) generalFeature;
        ArrayList<FeatureQualifier> qualifiers = new ArrayList<FeatureQualifier>();
        String value;
        for (String qualification : feature.getAllele()) {
            value = StringUtility.quote(qualification);
            if (FeatureFormatUtility.isEmptyQualifier(value) == false) qualifiers.add(new FeatureQualifier("allele", value));
        }
        for (String qualification : feature.getBoundMoieties()) {
            value = StringUtility.quote(qualification);
            if (FeatureFormatUtility.isEmptyQualifier(value) == false) qualifiers.add(new FeatureQualifier("bound_moiety", value));
        }
        value = StringUtility.toBracketedInt(feature.getCitation());
        if (FeatureFormatUtility.isEmptyQualifier(value) == false) qualifiers.add(new FeatureQualifier("citation", value));
        for (DatabaseCrossReference qualification : feature.getDatabaseCrossReferences()) {
            value = StringUtility.quote(FeatureFormatUtility.toString(qualification));
            if (FeatureFormatUtility.isEmptyQualifier(value) == false) qualifiers.add(new FeatureQualifier("db_xref", value));
        }
        value = feature.getDirection().getINSDCString();
        if (FeatureFormatUtility.isEmptyQualifier(value) == false) qualifiers.add(new FeatureQualifier("direction", value));
        value = StringUtility.quote(feature.getExperiment());
        if (FeatureFormatUtility.isEmptyQualifier(value) == false) qualifiers.add(new FeatureQualifier("experiment", value));
        value = StringUtility.quote(feature.getGene());
        if (FeatureFormatUtility.isEmptyQualifier(value) == false) qualifiers.add(new FeatureQualifier("gene", value));
        for (String qualification : feature.getGeneSynonyms()) {
            value = StringUtility.quote(qualification);
            if (FeatureFormatUtility.isEmptyQualifier(value) == false) qualifiers.add(new FeatureQualifier("gene_synonym", value));
        }
        value = FeatureFormatUtility.toString(feature.getInference());
        if (FeatureFormatUtility.isEmptyQualifier(value) == false) qualifiers.add(new FeatureQualifier("inference", value));
        value = feature.getLabel();
        if (FeatureFormatUtility.isEmptyQualifier(value) == false) qualifiers.add(new FeatureQualifier("label", value));
        value = StringUtility.quote(feature.getLocusTag());
        if (FeatureFormatUtility.isEmptyQualifier(value) == false) qualifiers.add(new FeatureQualifier("locus_tag", value));
        value = StringUtility.quote(feature.getMap());
        if (FeatureFormatUtility.isEmptyQualifier(value) == false) qualifiers.add(new FeatureQualifier("map", value));
        value = StringUtility.quote(feature.getNotes());
        if (FeatureFormatUtility.isEmptyQualifier(value) == false) qualifiers.add(new FeatureQualifier("note", value));
        for (String qualification : feature.getOldLocusTags()) {
            value = StringUtility.quote(qualification);
            if (FeatureFormatUtility.isEmptyQualifier(value) == false) qualifiers.add(new FeatureQualifier("old_locus_tag", value));
        }
        value = StringUtility.quote(feature.getRepeatFamily());
        if (FeatureFormatUtility.isEmptyQualifier(value) == false) qualifiers.add(new FeatureQualifier("rpt_family", value));
        value = feature.getRepeatType().getINSDCString();
        if (FeatureFormatUtility.isEmptyQualifier(value) == false) qualifiers.add(new FeatureQualifier("rpt_type", value));
        value = FeatureFormatUtility.toString(feature.getRepeatUnitRange());
        if (FeatureFormatUtility.isEmptyQualifier(value) == false) qualifiers.add(new FeatureQualifier("rpt_unit_range", value));
        value = StringUtility.quote(feature.getRepeatUnitSequence());
        if (FeatureFormatUtility.isEmptyQualifier(value) == false) qualifiers.add(new FeatureQualifier("rpt_unit_seq", value));
        value = StringUtility.quote(feature.getStandardName());
        if (FeatureFormatUtility.isEmptyQualifier(value) == false) qualifiers.add(new FeatureQualifier("standard_name", value));
        return qualifiers;
    }
}
