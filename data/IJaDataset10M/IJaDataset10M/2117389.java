package com.tll.model;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
import com.tll.model.bk.BusinessKeyDef;
import com.tll.model.bk.BusinessObject;
import com.tll.model.validate.BusinessKeyUniqueness;

/**
 * The Interface entity
 * @author jpk
 */
@Root
@BusinessObject(businessKeys = @BusinessKeyDef(name = "Code", properties = { "code" }))
public abstract class Interface extends NamedTimeStampEntity {

    private static final long serialVersionUID = 5959712644331302508L;

    static final String SWITCH_VALUE = "0";

    static final String SINGLE_VALUE = "1";

    static final String MULTI_VALUE = "2";

    public static final int MAXLEN_CODE = 50;

    public static final int MAXLEN_NAME = 50;

    public static final int MAXLEN_DESCRIPTION = 128;

    public static final String CODE_PAYMENT_PROCESSOR = "pymntproc";

    public static final String CODE_SHIP_METHOD = "shipmethod";

    public static final String CODE_SALES_TAX = "salestax";

    public static final String CODE_PAYMENT_METHOD = "pymntmethod";

    public static final String CODE_CROSS_SELL = "crosssell";

    protected String code;

    protected String description;

    protected boolean isAvailableAsp = false;

    protected boolean isAvailableIsp = false;

    protected boolean isAvailableMerchant = false;

    protected boolean isAvailableCustomer = false;

    protected boolean isRequiredAsp = false;

    protected boolean isRequiredIsp = false;

    protected boolean isRequiredMerchant = false;

    protected boolean isRequiredCustomer = false;

    protected Set<InterfaceOption> options = new LinkedHashSet<InterfaceOption>();

    @Override
    public final Class<? extends IEntity> rootEntityClass() {
        return Interface.class;
    }

    @NotEmpty
    @Length(max = MAXLEN_NAME)
    @Override
    public String getName() {
        return name;
    }

    /**
	 * @return Returns the code.
	 */
    @NotEmpty
    @Length(max = MAXLEN_CODE)
    public String getCode() {
        return code;
    }

    /**
	 * @param code The code to set.
	 */
    public void setCode(final String code) {
        this.code = code;
    }

    /**
	 * @return Returns the description.
	 */
    @NotEmpty
    @Length(max = MAXLEN_DESCRIPTION)
    public String getDescription() {
        return description;
    }

    /**
	 * @param description The description to set.
	 */
    public void setDescription(final String description) {
        this.description = description;
    }

    /**
	 * @return Returns the isAvailableAsp.
	 */
    @NotNull
    public boolean getIsAvailableAsp() {
        return isAvailableAsp;
    }

    /**
	 * @param isAvailableAsp The isAvailableAsp to set.
	 */
    public void setIsAvailableAsp(final boolean isAvailableAsp) {
        this.isAvailableAsp = isAvailableAsp;
    }

    /**
	 * @return Returns the isAvailableCustomer.
	 */
    @NotNull
    public boolean getIsAvailableCustomer() {
        return isAvailableCustomer;
    }

    /**
	 * @param isAvailableCustomer The isAvailableCustomer to set.
	 */
    public void setIsAvailableCustomer(final boolean isAvailableCustomer) {
        this.isAvailableCustomer = isAvailableCustomer;
    }

    /**
	 * @return Returns the isAvailableIsp.
	 */
    @NotNull
    public boolean getIsAvailableIsp() {
        return isAvailableIsp;
    }

    /**
	 * @param isAvailableIsp The isAvailableIsp to set.
	 */
    public void setIsAvailableIsp(final boolean isAvailableIsp) {
        this.isAvailableIsp = isAvailableIsp;
    }

    /**
	 * @return Returns the isAvailableMerchant.
	 */
    @NotNull
    public boolean getIsAvailableMerchant() {
        return isAvailableMerchant;
    }

    /**
	 * @param isAvailableMerchant The isAvailableMerchant to set.
	 */
    public void setIsAvailableMerchant(final boolean isAvailableMerchant) {
        this.isAvailableMerchant = isAvailableMerchant;
    }

    /**
	 * @return Returns the isRequiredAsp.
	 */
    @NotNull
    public boolean getIsRequiredAsp() {
        return isRequiredAsp;
    }

    /**
	 * @param isRequiredAsp The isRequiredAsp to set.
	 */
    public void setIsRequiredAsp(final boolean isRequiredAsp) {
        this.isRequiredAsp = isRequiredAsp;
    }

    /**
	 * @return Returns the isRequiredCustomer.
	 */
    @NotNull
    public boolean getIsRequiredCustomer() {
        return isRequiredCustomer;
    }

    /**
	 * @param isRequiredCustomer The isRequiredCustomer to set.
	 */
    public void setIsRequiredCustomer(final boolean isRequiredCustomer) {
        this.isRequiredCustomer = isRequiredCustomer;
    }

    /**
	 * @return Returns the isRequiredIsp.
	 */
    @NotNull
    public boolean getIsRequiredIsp() {
        return isRequiredIsp;
    }

    /**
	 * @param isRequiredIsp The isRequiredIsp to set.
	 */
    public void setIsRequiredIsp(final boolean isRequiredIsp) {
        this.isRequiredIsp = isRequiredIsp;
    }

    /**
	 * @return Returns the isRequiredMerchant.
	 */
    @NotNull
    public boolean getIsRequiredMerchant() {
        return isRequiredMerchant;
    }

    /**
	 * @param isRequiredMerchant The isRequiredMerchant to set.
	 */
    public void setIsRequiredMerchant(final boolean isRequiredMerchant) {
        this.isRequiredMerchant = isRequiredMerchant;
    }

    /**
	 * @return Returns the options.
	 */
    @BusinessKeyUniqueness(type = "option")
    @Valid
    public Set<InterfaceOption> getOptions() {
        return options;
    }

    /**
	 * @param options The options to set.
	 */
    public void setOptions(final Set<InterfaceOption> options) {
        this.options = options;
    }

    public InterfaceOption getOption(final Object pk) {
        return findEntityInCollection(options, pk);
    }

    public InterfaceOption getOption(final String nme) {
        return findNamedEntityInCollection(options, nme);
    }

    public void addOption(final InterfaceOption e) {
        addEntityToCollection(options, e);
    }

    public void addOptions(final Collection<InterfaceOption> clc) {
        addEntitiesToCollection(clc, options);
    }

    public void removeOption(final InterfaceOption e) {
        removeEntityFromCollection(options, e);
    }

    public void clearOptions() {
        clearEntityCollection(options);
    }

    public int getNumOptions() {
        return getCollectionSize(options);
    }
}
