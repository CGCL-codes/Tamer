package org.pachyderm.foundation.metadata;

import com.webobjects.foundation.NSArray;

/**
 * The DCMD class contains string contstants for the Dublin Core metadata keys.
 */
public final class DCMD {

    /**
	 *  Name:  Contributor
	 *  Desc:  An entity responsible for making contributions to the content of the resource. Examples of a Contributor include a person, an organization, or a service. Typically, the name of a Contributor should be used to indicate the entity.
	 */
    public static final String Contributor = "contributor";

    /**
	 *  Name:  Coverage
	 *  Desc:  The extent or scope of the content of the resource. Coverage will typically include
	 *         spatial location (a place name or geographic co-ordinates), temporal period 
	 *         (a period label, date, or date range) or jurisdiction (such as a named 
	 *         administrative entity). Recommended best practice is to select a value from a
	 *         controlled vocabulary (for example, the Thesaurus of Geographic Names [TGN]), and
	 *         that, where appropriate, named places or time periods be used in preference to 
	 *         numeric identifiers such as sets of co-ordinates or date ranges. 
	 */
    public static final String Coverage = "coverage";

    /** 
	 *  Name:  Creator
	 *  Desc:  An entity primarily responsible for making the content of the resource.
	 *		   Examples of a Creator include a person, an organization, or a service. 
	 *		   Typically, the name of a Creator should be used to indicate the entity.
	 *		   Application used to create the document content (e.g. "Word", "APOLLOWorks", etc.). 
	 */
    public static final String Creator = "creator";

    /**
	 *  Name:  Date
	 *  Desc:  A date associated with an event in the life cycle of the resource. Typically, Date will be associated 
	 *         with the creation or availability of the resource. Recommended best practice for encoding the date value 
	 *         is defined in a profile of ISO 8601 [W3CDTF] and follows the YYYY-MM-DD format.
	 */
    public static final String Date = "date";

    /**
	 *  Name:  Description
	 *  Desc:  A description of the content of the resource. Description may include but is not limited to: an abstract,
	 *         table of contents, reference to a graphical representation of content or a free-text account of the 
	 *         content.
	 */
    public static final String Description = "description";

    /**
	 *  Name:  Format
	 *  Desc:  The physical or digital manifestation of the resource. Typically, Format may include the media-type 
	 *         or dimensions of the resource. Format may be used to determine the software, hardware or other equipment 
	 *         needed to display or operate the resource. Examples of dimensions include size and duration. Recommended
	 *         best practice is to select a value from a controlled vocabulary (for example, the list of Internet Media
	 *         Types [MIME] defining computer media formats).
	 */
    public static final String Format = "format";

    /**
	 *  Name:  Identifier  
	 *  Desc:  An unambiguous reference to the resource within a given context. Recommended best practice is to 
	 *         identify the resource by means of a string or number conforming to a formal identification system. 
	 *         Example formal identification systems include the Uniform Resource Identifier (URI) (including the Uniform
	 *         Resource Locator (URL)), the Digital Object Identifier (DOI) and the International Standard Book Number 
	 *         (ISBN).
	 */
    public static final String Identifier = "identifier";

    /**
	 *  Name:  Language
	 *  Desc:  A language of the intellectual content of the resource. Recommended best practice is to use RFC 3066 
	 *         [RFC3066], which, in conjunction with ISO 639 [ISO639], defines two- and three-letter primary language 
	 *         tags with optional subtags. Examples include "en" or "eng" for English, "akk" for Akkadian, and "en-GB" 
	 *         for English used in the United Kingdom.
	 */
    public static final String Language = "language";

    /**
	 *  Name:  Publisher
	 *  Desc:  An entity responsible for making the resource available Examples of a Publisher include a person, 
	 *         an organization, or a service. Typically, the name of a Publisher should be used to indicate the entity.
	 */
    public static final String Publisher = "publisher";

    /**
	 *  Name:  Relation
	 *  Desc:  A reference to a related resource. Recommended best practice is to reference the resource by means of 
	 *         a string or number conforming to a formal identification system.
	 */
    public static final String Relation = "relation";

    /**
	 *  Name:  Rights
	 *  Desc:  Provides information or a link to information about rights held in and over the resource. Typically, 
	 *		   a Rights element will contain a rights management statement for the resource, or reference a service
	 *         providing such information. Rights information often encompasses Intellectual Property Rights (IPR), 
	 *         Copyright, and various Property Rights. If this attribute is absent, no assumptions can be made about 
	 *         the status of these and other rights with respect to the resource. 
	 */
    public static final String Rights = "rights";

    /**
	 *  Name:  Source
	 *  Desc:  A reference to a resource from which the present resource is derived. The present resource may be 
	 *         derived from the Source resource in whole or in part. Recommended best practice is to reference the 
	 *         resource by means of a string or number conforming to a formal identification system.
	 */
    public static final String Source = "source";

    /**
	 *  Name:  Subject
	 *  Class: Dublin Core Metadata Initiative
	 *  Desc:  The topic of the content of the resource. Typically, a Subject will be expressed as keywords, key 
	 *         phrases or classification codes that describe a topic of the resource. Recommended best practice is 
	 *         to select a value from a controlled vocabulary or formal classification scheme.
	 */
    public static final String Subject = "subject";

    /**
	 *  Name:  Title
	 *  Desc:  A name given to the resource, or the title of the file. Typically, a Title will be a name by which the resource is formally known. For example, this could be the title of a document, the name of an song, or the subject of an email message. 
	 */
    public static final String Title = "title";

    /**
	 *  Name:  Type
	 *  Desc:  The nature or genre of the content of the resource. Type includes terms describing general categories, 
	 *         functions, genres, or aggregation levels for content. Recommended best practice is to select a value from
	 *         a controlled vocabulary (for example, the DCMI Type Vocabulary [DCMITYPE]). To describe the physical or 
	 *         digital manifestation of the resource, use the Format element. 
	 *         [DCMITYPE] http://dublincore.org/documents/dcmi-type-vocabulary/
	 */
    public static final String Type = "type";

    /**
	 *  Name: DescriptionAbstract 
	 *  Desc:  A summary of the content of the resource.
	 */
    public static final String DescriptionAbstract = "abstract";

    /**
	 *  Name:  RightsAccessRights  
	 *  Desc:  Information about who can access the resource or an indication of its security status. Access Rights 
	 *		   may include information regarding access or restrictions based on privacy, security or other regulations.
	 */
    public static final String RightsAccessRights = "accessRights";

    /**
	 *  Name:  AccrualMethod
	 *  Desc:  The method by which items are added to a collection. Recommended best practice is to use a value from 
	 *         a controlled vocabulary.
	 */
    public static final String AccrualMethod = "accrualMethod";

    /**
	 *  Name:  AccrualPeriodicity
	 *  Desc:  The frequency with which items are added to a collection. Recommended best practice is to use a value 
	 *         from a controlled vocabulary.
	 */
    public static final String AccrualPeriodicity = "accrualPeriodicity";

    /**
	 *  Name:  AccrualPolicy
	 *  Desc:  The policy governing the addition of items to a collection. Recommended best practice is to use a value 
	 *         from a controlled vocabulary.
	 */
    public static final String AccrualPolicy = "accrualPolicy";

    /**
	 *  Name:  TitleAlternative
	 *  Desc:  Any form of the title used as a substitute or alternative to the formal title of the resource. This
	 *         qualifier can include Title abbreviations as well as translations.
	 */
    public static final String TitleAlternative = "titleAlternative";

    /**
	 *  Name:  Audience
	 *  Desc:  A class of entity for whom the resource is intended or useful. A class of entity may be determined by 
	 *         the creator or the publisher or by a third party.
	 */
    public static final String Audience = "audience";

    /**
	 *  Name:  DateAvailable
	 *  Desc:  Date (often a range) that the resource will become or did become available.
	 */
    public static final String DateAvailable = "dateAvailable";

    /**
	 *  Name:  IdentifierBibliographicCitation
	 *  Desc:  A bibliographic reference for the resource. Recommended practice is to include sufficient 
	 *         bibliographic detail to identify the resource as unambiguously as possible, whether or not the citation 
	 *         is in a standard form.
	 */
    public static final String IdentifierBibilographicCitation = "bibliographicCitation";

    /**
	 *  Name:  RelationConformsTo
	 *  Class: Dublin Core Metadata Initiative
	 *  Desc:  A reference to an established standard to which the resource conforms.
	 */
    public static final String RelationConformsTo = "conformsTo";

    /**
	 *  Name:  DateCreated
	 *  Desc:  Date of creation of the resource.
	 */
    public static final String DateCreated = "dateCreated";

    /**
	 *  Name:  DateAccepted
	 *  Desc:  Date of acceptance of the resource (e.g. of thesis by university department, of article by journal, etc.).
	 */
    public static final String DateAccepted = "dateAccepted";

    /**
	 *  Name:  DateCopyrighted
	 *  Desc:  Date of a statement of copyright.
	 */
    public static final String DateCopyrighted = "dateCopyrighted";

    /**
	 *  Name:  DateSubmitted
	 *  Desc:  Date of submission of the resource (e.g. thesis, articles, etc.).
	 */
    public static final String DateSubmitted = "dateSubmitted";

    /**
	 *  Name:  AudienceEducationLevel
	 *  Desc:  A general statement describing the education or training context. Alternatively, a more specific statement of the location of the audience in terms of its progression through an education or training context.
	 */
    public static final String AudienceEducationLevel = "audienceEducationLevel";

    /**
	 *  Name:  FormatExtent
	 *  Desc:  The size or duration of the resource.
	 */
    public static final String FormatExtent = "formatExtent";

    /**
	 *  Name:  RelationHasFormat
	 *  Desc:  The described resource pre-existed the referenced resource, which is essentially the same intellectual
	 *		   content presented in another format.
	 */
    public static final String RelationHasFormat = "hasFormat";

    /**
	 *  Name:  RelationHasPart
	 *  Desc:  The described resource includes the referenced resource either physically or logically.
	 */
    public static final String RelationHasPart = "hasPart";

    /**
	 *  Name:  RelationHasVersion
	 *  Desc:  The described resource has a version, edition, or adaptation, namely, the referenced resource.
	 */
    public static final String RelationHasVersion = "hasVersion";

    /**
	 *  Name:  InstructionalMethod
	 *  Desc:  A process, used to engender knowledge, attitudes and skills, that the resource is designed to support.
	 *		   Instructional Method will typically include ways of presenting instructional materials or conducting
	 *         instructional activities, patterns of learner-to-learner and learner-to-instructor interactions, and 
	 *         mechanisms by which group and individual levels of learning are measured. Instructional methods include 
	 *         all aspects of the instruction and learning processes from planning and implementation through 
	 *         evaluation and feedback.
	 */
    public static final String InstructionalMethod = "instructionalMethod";

    /**
	 *  Name:  RelationIsFormatOf
	 *  Desc:  The described resource is the same intellectual content of the referenced resource, but presented in 
	 *		   another format.
	 */
    public static final String RelationIsFormatOf = "isFormatOf";

    /**
	 *  Name:  RelationIsPartOf
	 *  Desc:  The described resource is a physical or logical part of the referenced resource.
	 */
    public static final String RelationIsPartOf = "isPartOf";

    /**
	 *  Name:  RelationIsReferencedBy
	 *  Desc:  The described resource is referenced, cited, or otherwise pointed to by the referenced resource.
	 */
    public static final String RelationIsReferencedBy = "isReferencedBy";

    /**
	 *  Name:  RelationIsReplacedBy
	 *  Desc:  The described resource is supplanted, displaced, or superseded by the referenced resource.
	 */
    public static final String RelationIsReplacedBy = "isReplacedBy";

    /**
	 *  Name:  RelationIsRequiredBy
	 *  Desc:  The described resource is required by the referenced resource, either physically or logically.
	 */
    public static final String RelationIsRequiredBy = "isRequiredBy";

    /**
	 *  Name:  DateIssued 
	 *  Desc:  Date of formal issuance (e.g., publication) of the resource.
	 */
    public static final String DateIssued = "dateIssued";

    /**
	 *  Name:  RelationIsVersionOf
	 *  Desc:  The described resource is a version, edition, or adaptation of the referenced resource. Changes in 
	 *		   version imply substantive changes in content rather than differences in format.
	 */
    public static final String RelationIsVersionOf = "isVersionOf";

    public static final String RightsLicense = "license";

    /**
	 *  Name:  AudienceMediator
	 *  Desc:  A class of entity that mediates access to the resource and for whom the resource is intended or useful.
	 *		   The audiences for a resource are of two basic classes: (1) an ultimate beneficiary of the resource, and 
	 *         (2) frequently, an entity that mediates access to the resource. The mediator element refinement 
	 *		   represents the second of these two classes.
	 */
    public static final String AudienceMediator = "mediator";

    /**
	 *  Name:  FormatMedium
	 *  Desc:  The material or physical carrier of the resource.
	 */
    public static final String FormatMedium = "medium";

    /**
	 *  Name:  DateModified
	 *  Desc:  Date on which the resource was changed.
	 */
    public static final String DateModified = "dateModified";

    /**
	 *  Name:  Provenance
	 *  Desc:  A statement of any changes in ownership and custody of the resource since its creation that are 
	 *	       significant for its authenticity, integrity and interpretation. The statement may include a description 
	 *         of any changes successive custodians made to the resource.
	 */
    public static final String Provenance = "provenance";

    /**
	 *  Name:  RelationReferences
	 *  Desc:  The described resource references, cites, or otherwise points to the referenced resource.
	 */
    public static final String RelationReferences = "references";

    /**
	 *  Name:  RelationReplaces
	 *  Desc:  The described resource supplants, displaces, or supersedes the referenced resource.
	 */
    public static final String RelationReplaces = "replaces";

    /**
	 *  Name:  RelationRequires
	 *  Desc:  The described resource requires the referenced resource to support its function, delivery, or coherence 
	 *         of content.
	 */
    public static final String RelationRequires = "requires";

    /**
	 *  Name:  RightsHolder
	 *  Desc:  A person or organization owning or managing rights over the resource. Recommended best practice is to 
	 *         use the URI or name of the Rights Holder to indicate the entity.
	 */
    public static final String RightsHolder = "rightsHolder";

    /**
	 *  Name:  CoverageSpatial
	 *  Desc:  Spatial characteristics of the intellectual content of the resource.
	 */
    public static final String CoverageSpatial = "coverageSpatial";

    /**
	 *  Name:  DescriptionTableOfContents
	 *  Desc:  Table Of Contents. A list of subunits of the content of the resource.
	 */
    public static final String DescriptionTableOfContents = "tableOfContents";

    /**
	 *  Name:  CoverageTemporal
	 *  Desc:  Temporal characteristics of the intellectual content of the resource.
	 */
    public static final String CoverageTemporal = "coverageTemporal";

    /**
	 *  Name:  DateValid
	 *  Desc:  Date (often a range) of validity of a resource.
	 */
    public static final String DateValid = "dateValid";

    public final NSArray DCMIBoxEncodingScheme = new NSArray(new Object[] { "northlimit", "eastlimit", "southlimit", "westlimit", "uplimit", "downlimit", "units", "zunits", "projection", "name" });

    public final NSArray DCMITypeVocabulary = new NSArray(new Object[] { "Collection", "Dataset", "Event", "Image", "InteractiveResource", "MovingImage", "PhysicalObject", "Service", "Software", "Sound", "StillImage", "Text" });

    public final NSArray DCMIPeriodEncodingScheme = new NSArray(new Object[] { "start", "end", "scheme", "name" });

    public final NSArray DCMIPointEncodingScheme = new NSArray(new Object[] { "east", "north", "elevation", "units", "zunits", "projection", "name" });
}
