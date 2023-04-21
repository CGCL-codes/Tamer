package org.openuss.registration;

/**
 * 
 */
public interface InstituteActivationCode extends org.openuss.registration.ActivationCode, org.openuss.foundation.DomainObject {

    /**
     * 
     */
    public String getCode();

    public void setCode(String code);

    /**
	 * Modified
	 * 
	 */
    public org.openuss.lecture.Institute getInstitute();

    public void setInstitute(org.openuss.lecture.Institute institute);

    /**
	 * Constructs new instances of
	 * {@link org.openuss.registration.InstituteActivationCode}.
	 */
    public abstract static class Factory {

        /**
		 * Singleton instance of the concrete factory
		 */
        private static InstituteActivationCode.Factory factory = null;

        /**
		 * Singleton method to obtain an instance of the concrete factory
		 */
        private static InstituteActivationCode.Factory getFactory() {
            if (factory == null) {
                factory = (InstituteActivationCode.Factory) org.openuss.api.utilities.FactoryFinder.find("org.openuss.registration.InstituteActivationCode.Factory");
            }
            return factory;
        }

        /**
		 * Abstract factory method for the concrete product.
		 */
        public abstract InstituteActivationCode createInstituteActivationCode();

        /**
		 * Constructs a new instance of
		 * {@link org.openuss.registration.InstituteActivationCode}.
		 */
        public static org.openuss.registration.InstituteActivationCode newInstance() {
            return getFactory().createInstituteActivationCode();
        }

        /**
		 * Constructs a new instance of
		 * {@link org.openuss.registration.InstituteActivationCode}, taking all
		 * required and/or read-only properties as arguments.
		 */
        public static org.openuss.registration.InstituteActivationCode newInstanceByIdentifier(Long id) {
            final org.openuss.registration.InstituteActivationCode entity = getFactory().createInstituteActivationCode();
            entity.setId(id);
            return entity;
        }

        /**
		 * Constructs a new instance of
		 * {@link org.openuss.registration.InstituteActivationCode}, taking all
		 * possible properties (except the identifier(s))as arguments.
		 */
        public static org.openuss.registration.InstituteActivationCode newInstance(String code, java.sql.Timestamp createdAt, org.openuss.lecture.Institute institute) {
            final org.openuss.registration.InstituteActivationCode entity = getFactory().createInstituteActivationCode();
            entity.setCode(code);
            entity.setCreatedAt(createdAt);
            entity.setInstitute(institute);
            return entity;
        }
    }
}
