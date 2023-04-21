package bgl.exceptions;

public class SourceCodeException extends BglException {

    private static final long serialVersionUID = 6735618752326756166L;

    public static class DoubleSpaceException extends SourceCodeException {

        private static final long serialVersionUID = -2337747353728099004L;

        public DoubleSpaceException(String message) {
            this.message = message;
        }
    }

    public static class InvalidBracketOrderException extends SourceCodeException {

        private static final long serialVersionUID = -6858394789413286186L;

        public InvalidBracketOrderException(String message) {
            this.message = message;
        }
    }

    public static class TypeNotFoundException extends SourceCodeException {

        private static final long serialVersionUID = 478376448153463997L;

        public TypeNotFoundException(String message) {
            this.message = message;
        }
    }

    public static class InvalidVariableNameException extends SourceCodeException {

        private static final long serialVersionUID = -6956121202513348628L;

        public InvalidVariableNameException(String message) {
            this.message = message;
        }
    }

    public static class InvalidClassNameException extends SourceCodeException {

        private static final long serialVersionUID = -3526786726404518726L;

        public InvalidClassNameException(String message) {
            this.message = message;
        }
    }

    public static class InvalidSuperClassNameException extends SourceCodeException {

        private static final long serialVersionUID = -8641982758864571227L;

        public InvalidSuperClassNameException(String message) {
            this.message = message;
        }
    }

    public static class InvalidLocationException extends SourceCodeException {

        private static final long serialVersionUID = 5914880204632861283L;

        public InvalidLocationException(String message) {
            this.message = message;
        }
    }

    public static class InvalidSyntaxException extends SourceCodeException {

        private static final long serialVersionUID = 5839825176478314061L;

        public InvalidSyntaxException(String message) {
            this.message = message;
        }
    }

    public static class FunctionReturnTypeDeclarationNotFoundException extends SourceCodeException {

        private static final long serialVersionUID = -8450488919640374836L;

        public FunctionReturnTypeDeclarationNotFoundException(String message) {
            this.message = message;
        }
    }

    public static class FunctionDeclarationNotFoundException extends SourceCodeException {

        private static final long serialVersionUID = 4593634627577194213L;

        public FunctionDeclarationNotFoundException(String message) {
            this.message = message;
        }
    }

    public static class InvalidVariableDeclarationException extends SourceCodeException {

        private static final long serialVersionUID = 2606808713199933232L;

        public InvalidVariableDeclarationException(String message) {
            this.message = message;
        }
    }

    public static class UnificationAmbiguityException extends SourceCodeException {

        private static final long serialVersionUID = 6761943795698443183L;

        public UnificationAmbiguityException(String message) {
            this.message = message;
        }
    }

    public static class NoSectionPossibleException extends SourceCodeException {

        private static final long serialVersionUID = 6449118981594942064L;

        public NoSectionPossibleException(String message) {
            this.message = message;
        }
    }

    public static class ClassNameAlreadySetException extends SourceCodeException {

        private static final long serialVersionUID = -1069355188982374438L;

        public ClassNameAlreadySetException(String message) {
            this.message = message;
        }
    }

    public static class SuperClassAlreadySetException extends SourceCodeException {

        private static final long serialVersionUID = -6815959317192603148L;

        public SuperClassAlreadySetException(String message) {
            this.message = message;
        }
    }

    public static class InvalidFunctionDeclarationException extends SourceCodeException {

        private static final long serialVersionUID = 7262418899155039122L;

        public InvalidFunctionDeclarationException(String message) {
            this.message = message;
        }
    }

    public static class InvalidReturnType extends SourceCodeException {

        private static final long serialVersionUID = 4602591652543791597L;

        public InvalidReturnType(String message) {
            this.message = message;
        }
    }

    public static class InstructionSectionMismatchException extends SourceCodeException {

        private static final long serialVersionUID = 4718895145287176017L;

        public InstructionSectionMismatchException(String message) {
            this.message = message;
        }
    }
}
