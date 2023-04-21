package com.volantis.mcs.utilities;

/**
 * Generic constants that identify types of fault.
 *
 * Note this class should be removed when the validation message generation
 * mechanism is replaced.
 */
public interface FaultTypes {

    /**
     * Fault type constants.
     */
    String CANNOT_BE_NULL = "cannotBeNull";

    String DUPLICATE_ASSET = "duplicateAsset";

    String DUPLICATE_NAME = "duplicateName";

    String INVALID_CHARACTER = "invalidCharacter";

    String INVALID_CHARACTERS = "invalidCharacters";

    String INVALID_COLOR = "invalidColor";

    String INVALID_DIRECTORY = "invalidDirectory";

    String INVALID_DEVICE_REPOSITORY_FILENAME = "invalidDeviceRepositoryFilename";

    String INVALID_FIRST_CHARACTER = "invalidFirstCharacter";

    String INVALID_FILENAME = "invalidFilename";

    String INVALID_HOUR = "invalidHour";

    String INVALID_TEXT = "invalidText";

    String INVALID_EXTENSION = "invalidExtension";

    String INVALID_MINUTE = "invalidMinute";

    String INVALID_SECOND = "invalidSecond";

    String INVALID_TIME = "invalidTime";

    String LESS_THAN = "lessThan";

    String LESS_THAN_ZERO = "lessThanZero";

    String MORE_THAN = "moreThan";

    String MUST_BE_IN = "mustBeIn";

    String MUST_NOT_BE_IN = "mustNotBeIn";

    String MUST_START_WITH = "mustStartWith";

    String NOT_A_NUMBER = "notANumber";

    String NOT_IN_PROJECT = "notInProject";

    String NOT_IN_REPOSITORY = "notInRepository";

    String NOT_IN_SELECTION = "notInSelection";

    String NOT_WRITEABLE = "notWriteable";

    String OUT_OF_RANGE = "outOfRange";

    String TOO_FEW_CHARACTERS = "tooFewCharacters";

    String TOO_MANY_CHARACTERS = "tooManyCharacters";

    String TOO_MANY_IN = "tooManyIn";

    String UNEXPECTED_END = "unexpectedEnd";

    String ZERO_TIME = "zeroTime";

    /**
     * Constant for an invalid element location error
     */
    String INVALID_ELEMENT_LOCATION = "invalidElementLocation";

    /**
     * Constant for an invalid element content error
     */
    String INVALID_ELEMENT_CONTENT = "invalidElementContent";

    /**
     * Constant for an invalid attribute location error
     */
    String INVALID_ATTRIBUTE_LOCATION = "invalidAttributeLocation";

    /**
     * Constant for an invalid attribute content error
     */
    String INVALID_ATTRIBUTE_CONTENT = "invalidAttributeContent";

    /**
     * Constant for an invalid value for a schema data type
     */
    String INVALID_SCHEMA_DATA_TYPE = "invalidSchemaDataType";

    /**
     * Constant for an invalid value for a policy name.
     */
    String INVALID_POLICY_NAME = "invalidPolicyName";

    /**
     * Constant for an invalid value for schema pattern value
     */
    String INVALID_SCHEMA_PATTERN_VALUE = "invalidSchemaPatternValue";

    /**
     * Constant for a violation of a schema constraint
     */
    String SCHEMA_CONSTRAINT_VIOLATED = "schemaConstraintViolated";

    /**
     * Constant for error that occurs when a required attribute is missing.
     */
    String MISSING_ATTRIBUTE = "missingAttribute";

    /**
     * Constant for when a max inclusive constraint is violated. This occurs
     * when a numeric value exceeds a specified upper bound
     */
    String MAX_INCLUSIVE_VIOLATED = "maxInclusiveViolated";

    /**
     * Constant for when a min inclusive constraint is violated. This occurs
     * when a numeric value exceeds a specified lower bound
     */
    String MIN_INCLUSIVE_VIOLATED = "minInclusiveViolated";

    /**
     * Constant for when specifing the minimum value of a range that is more
     * than the maximum value for the range.
     */
    String MIN_RANGE_MORE_THAN_MAX = "minRangeMoreThanMax";

    /**
     * Constant for when a max length constraint is violated
     */
    String MAX_LENGTH_VIOLATED = "maxLengthViolated";

    /**
     * Constant for when the value is not in the available selection.
     */
    String INVALID_SELECTION = "invalidSelection";

    /**
     * Constant for when a null asset and a targetable asset target thw
     * same device
     */
    String INVALID_NULL_ASSET = "invalidNullAsset";

    /**
     * Constant for an unknown XML validation error. This is used as a last
     * resort   "catch-all" error.
     */
    String UNKNOWN_INVALID_XML = "unknownInvalidXML";

    /**
     * Constant for when a unique constraint is violated.
     */
    String DUPLICATE_UNIQUE = "duplicateUnique";

    /**
     * Constant for when a value is required that is non-whitespace and
     * non-empty
     */
    String WHITESPACE = "whitespace";
}
