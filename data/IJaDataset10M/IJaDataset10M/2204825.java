package org.torweg.pulse.component.shop.checkout.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collection;
import java.util.Collections;
import java.util.Currency;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.hibernate.annotations.Sort;
import org.hibernate.annotations.SortType;
import org.torweg.pulse.component.shop.model.ShoppingCart.Total;
import org.torweg.pulse.component.shop.model.TaxGroup;
import org.torweg.pulse.util.entity.AbstractBasicEntity;
import org.torweg.pulse.util.xml.bind.CurrencyXmlAdapter;

/**
 * The {@code TotalPrice}.
 * 
 * @author Christian Schatt
 * @version $Revision$
 */
@Entity(name = "checkout_TotalPrice")
@XmlRootElement(name = "total-price")
@XmlAccessorType(XmlAccessType.NONE)
public class TotalPrice extends AbstractBasicEntity implements Comparable<TotalPrice> {

    /**
	 * The {@code serialVersionUID}.
	 */
    private static final long serialVersionUID = -4705635241795064433L;

    /**
	 * The amount quotas, which means either the net amount quotas or the gross
	 * amount quotas, according to the net-based-flag. TODO: complete JavaDoc
	 */
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @Sort(type = SortType.NATURAL)
    @XmlElementWrapper(name = "amount-quotas", nillable = false, required = true)
    @XmlElement(name = "quota", nillable = false, required = true)
    private final SortedSet<Quota> amountQuotas = new TreeSet<Quota>();

    /**
	 * The {@code Currency}.
	 */
    @Column(nullable = false, updatable = false)
    @XmlElement(name = "currency", nillable = false, required = true)
    @XmlJavaTypeAdapter(CurrencyXmlAdapter.class)
    private Currency currency;

    /**
	 * The net-based-flag, indicating whether this {@code TotalPrice} is net
	 * based or gross based.
	 */
    @Column(nullable = false, updatable = false)
    @XmlElement(name = "net-based", nillable = false, required = true)
    private boolean netBased;

    /**
	 * The amount, which means either the net amount or the gross amount,
	 * according to the net-based-flag.
	 */
    @Column(nullable = false, updatable = false)
    private BigDecimal amount;

    @Column(nullable = false, updatable = true)
    private BigDecimal netAmount;

    @Column(nullable = false, updatable = true)
    private BigDecimal grossAmount;

    @Column(nullable = false, updatable = true)
    private BigDecimal taxAmount;

    /**
	 * The tax quotas. TODO: complete JavaDoc
	 */
    @Transient
    private final SortedSet<Quota> taxQuotas = new TreeSet<Quota>();

    /**
	 * The initialized-flag, indicating whether the amount, the net amount, the
	 * gross amount, the tax quotas and the tax amount have already been
	 * calculated and set.
	 */
    @Transient
    private boolean initialized;

    /**
	 * The no-argument constructor used by JAXB and JPA.
	 */
    @Deprecated
    protected TotalPrice() {
        super();
    }

    /**
	 * The copy constructor.
	 * 
	 * @param tp
	 *            the {@code TotalPrice} to be copied.
	 * @throws IllegalArgumentException
	 *             if the given {@code TotalPrice} is {@code null}.
	 */
    public TotalPrice(final TotalPrice tp) {
        super();
        if (tp == null) {
            throw new IllegalArgumentException("The given TotalPrice is null.");
        }
        for (Quota quota : tp.getAmountQuotas()) {
            this.amountQuotas.add(new Quota(quota));
        }
        this.currency = tp.getCurrency();
        this.netBased = tp.isNetBased();
        this.amount = tp.getAmount();
        this.netAmount = tp.getNetAmount();
        this.grossAmount = tp.getGrossAmount();
        for (Quota quota : tp.getTaxQuotas()) {
            this.taxQuotas.add(new Quota(quota));
        }
        this.taxAmount = tp.getTaxAmount();
        this.initialized = tp.initialized;
    }

    /**
	 * Creates a new {@code TotalPrice} from the given {@code Price}.
	 * 
	 * @param p
	 *            the {@code Price}.
	 * @throws IllegalArgumentException
	 *             if the given {@code Price} is {@code null}.
	 */
    public TotalPrice(final Price p) {
        super();
        if (p == null) {
            throw new IllegalArgumentException("The given Price is null.");
        }
        this.amountQuotas.add(new Quota(p.getTaxRate(), p.getAmount()));
        this.currency = p.getCurrency();
        this.netBased = p.isNetBased();
        this.amount = p.getAmount();
        this.netAmount = p.getNetAmount();
        this.grossAmount = p.getGrossAmount();
        this.taxQuotas.add(new Quota(p.getTaxRate(), p.getTaxAmount()));
        this.taxAmount = p.getTaxAmount();
        this.initialized = true;
    }

    /**
	 * Creates a new {@code TotalPrice} from the given amount quotas,
	 * {@code Currency} and net-based-flag.
	 * 
	 * @param aq
	 *            the amount quotas.
	 * @param c
	 *            the {@code Currency}.
	 * @param nb
	 *            the net-based-flag.
	 * @throws IllegalArgumentException
	 *             if the given {@code Map} holding the amount quotas is
	 *             {@code null} or empty or the given {@code Currency} is
	 *             {@code null}.
	 */
    public TotalPrice(final Map<? extends TaxRate, ? extends BigDecimal> aq, final Currency c, final boolean nb) {
        super();
        if ((aq == null) || aq.isEmpty()) {
            throw new IllegalArgumentException("The given Map holding the amount quotas is null or empty.");
        }
        if (c == null) {
            throw new IllegalArgumentException("The given Currency is null.");
        }
        for (Map.Entry<? extends TaxRate, ? extends BigDecimal> entry : aq.entrySet()) {
            this.amountQuotas.add(new Quota(entry.getKey(), entry.getValue()));
        }
        this.currency = c;
        this.netBased = nb;
        setAmounts();
    }

    /**
	 * Creates a new {@code TotalPrice} from the given {@code Price}s,
	 * {@code Currency} and net-based-flag.
	 * 
	 * @param p
	 *            the {@code Price}s.
	 * @param c
	 *            the {@code Currency}.
	 * @param nb
	 *            the net-based-flag.
	 * @throws IllegalArgumentException
	 *             if the given {@code Collection} of {@code Price}s is
	 *             {@code null} or empty, the given {@code Currency} is
	 *             {@code null} or one of the given {@code Price}s'
	 *             {@code Currency}s is not equal to the given {@code Currency}.
	 */
    public TotalPrice(final Collection<? extends Price> p, final Currency c, final boolean nb) {
        super();
        if ((p == null) || p.isEmpty()) {
            throw new IllegalArgumentException("The given Collection of Prices is null or empty.");
        }
        if (c == null) {
            throw new IllegalArgumentException("The given Currency is null.");
        }
        for (Price price : p) {
            if (!price.getCurrency().equals(c)) {
                throw new IllegalArgumentException(new StringBuilder("The Currency of the given Price '").append(price).append("' is not equal to the given Currency '").append(c).append("'.").toString());
            }
            Quota newQuota;
            if (nb) {
                newQuota = new Quota(price.getTaxRate(), price.getNetAmount());
            } else {
                newQuota = new Quota(price.getTaxRate(), price.getGrossAmount());
            }
            Quota oldQuota = getAmountQuota(price.getTaxRate());
            if (oldQuota == null) {
                this.amountQuotas.add(newQuota);
            } else {
                this.amountQuotas.remove(oldQuota);
                this.amountQuotas.add(oldQuota.add(newQuota));
            }
        }
        this.currency = c;
        this.netBased = nb;
        setAmounts();
    }

    /**
	 * Do not use this constructor. it produces rounding errors!!!
	 * 
	 * Creates a new {@code TotalPrice} from the given {@code Total},
	 * {@code Currency} and net-based-flag.
	 * 
	 * @param t
	 *            the {@code Total}.
	 * @param c
	 *            the {@code Currency}.
	 * @param nb
	 *            the net-based-flag.
	 * @throws IllegalArgumentException
	 *             if the given {@code Total} or {@code Currency} is
	 *             {@code null}.
	 */
    @Deprecated
    public TotalPrice(final Total t, final Currency c, final boolean nb) {
        super();
        if (t == null) {
            throw new IllegalArgumentException("The given Total is null.");
        }
        if (c == null) {
            throw new IllegalArgumentException("The given Currency is null.");
        }
        for (Map.Entry<TaxGroup, Long> entry : t.getTaxTotals().entrySet()) {
            TaxRate taxRate = new TaxRate(entry.getKey());
            BigDecimal taxAmnt = BigDecimal.valueOf(entry.getValue(), c.getDefaultFractionDigits());
            BigDecimal amnt;
            if (nb) {
                amnt = taxAmnt.divide(taxRate.getPercentage().scaleByPowerOfTen(-2), c.getDefaultFractionDigits(), RoundingMode.HALF_UP);
            } else {
                amnt = taxAmnt.divide(taxRate.getPercentage().scaleByPowerOfTen(-2), c.getDefaultFractionDigits(), RoundingMode.HALF_UP).add(taxAmnt);
            }
            Quota oldQuota = getAmountQuota(taxRate);
            if (oldQuota == null) {
                this.amountQuotas.add(new Quota(taxRate, amnt));
            } else {
                this.amountQuotas.remove(oldQuota);
                this.amountQuotas.add(new Quota(taxRate, amnt.add(oldQuota.getAmount())));
            }
        }
        this.currency = c;
        this.netBased = nb;
        setAmounts();
    }

    /**
	 * Returns a {@code SortedSet} holding the amount quotas.
	 * 
	 * @return the amount quotas.
	 */
    public final SortedSet<Quota> getAmountQuotas() {
        return Collections.unmodifiableSortedSet(this.amountQuotas);
    }

    /**
	 * Returns the amount quota for the given {@code TaxRate}.
	 * 
	 * @param tr
	 *            the {@code TaxRate}.
	 * @return the amount quota.
	 * @throws IllegalArgumentException
	 *             if the given {@code TaxRate} is {@code null}.
	 */
    public final Quota getAmountQuota(final TaxRate tr) {
        if (tr == null) {
            throw new IllegalArgumentException("The given TaxRate is null.");
        }
        for (Quota quota : this.amountQuotas) {
            if (quota.getTaxRate().equals(tr)) {
                return quota;
            }
        }
        return null;
    }

    /**
	 * Returns the {@code Currency}.
	 * 
	 * @return the {@code Currency}.
	 */
    public final Currency getCurrency() {
        return this.currency;
    }

    /**
	 * Returns the net-based-flag, indicating whether this {@code TotalPrice} is
	 * net based or gross based.
	 * 
	 * @return the net-based-flag.
	 */
    public final boolean isNetBased() {
        return this.netBased;
    }

    /**
	 * Returns the amount.
	 * 
	 * @return the amount.
	 */
    @XmlElement(name = "amount")
    public final BigDecimal getAmount() {
        if (!this.initialized) {
            setAmounts();
        }
        return this.amount;
    }

    /**
	 * Returns the net amount.
	 * 
	 * @return the net amount.
	 */
    @XmlElement(name = "net-amount")
    public final BigDecimal getNetAmount() {
        if (!this.initialized) {
            setAmounts();
        }
        return this.netAmount;
    }

    /**
	 * Returns the gross amount.
	 * 
	 * @return the gross amount.
	 */
    @XmlElement(name = "gross-amount")
    public final BigDecimal getGrossAmount() {
        if (!this.initialized) {
            setAmounts();
        }
        return this.grossAmount;
    }

    /**
	 * Returns the tax quotas.
	 * 
	 * @return the tax quotas.
	 */
    @XmlElementWrapper(name = "tax-quotas")
    @XmlElement(name = "quota")
    public final SortedSet<Quota> getTaxQuotas() {
        if (!this.initialized) {
            setAmounts();
        }
        return Collections.unmodifiableSortedSet(this.taxQuotas);
    }

    /**
	 * Returns the tax quota for the given {@code TaxRate}.
	 * 
	 * @param tr
	 *            the {@code TaxRate}.
	 * @return the tax quota.
	 * @throws IllegalArgumentException
	 *             if the given {@code TaxRate} is {@code null}.
	 */
    public final Quota getTaxQuota(final TaxRate tr) {
        if (tr == null) {
            throw new IllegalArgumentException("The given TaxRate is null.");
        }
        if (!this.initialized) {
            setAmounts();
        }
        for (Quota quota : this.taxQuotas) {
            if (quota.getTaxRate().equals(tr)) {
                return quota;
            }
        }
        return null;
    }

    /**
	 * Returns the tax amount.
	 * 
	 * @return the tax amount.
	 */
    @XmlElement(name = "tax-amount")
    public final BigDecimal getTaxAmount() {
        if (!this.initialized) {
            setAmounts();
        }
        return this.taxAmount;
    }

    /**
	 * Calculates and sets the amount, the net amount, the gross amount, the tax
	 * quotas and the tax amount.
	 */
    private void setAmounts() {
        BigDecimal amnt = BigDecimal.ZERO;
        SortedSet<Quota> taxQuts = new TreeSet<Quota>();
        BigDecimal taxAmnt = BigDecimal.ZERO;
        for (Quota quota : this.amountQuotas) {
            amnt = amnt.add(quota.getAmount());
            BigDecimal tax;
            if (this.netBased) {
                tax = quota.getAmount().multiply(quota.getTaxRate().getPercentage().scaleByPowerOfTen(-2).add(BigDecimal.ONE)).setScale(this.currency.getDefaultFractionDigits(), RoundingMode.HALF_UP).subtract(quota.getAmount());
            } else {
                tax = quota.getAmount().subtract(quota.getAmount().divide(quota.getTaxRate().getPercentage().scaleByPowerOfTen(-2).add(BigDecimal.ONE), this.currency.getDefaultFractionDigits(), RoundingMode.HALF_UP));
            }
            taxQuts.add(new Quota(quota.getTaxRate(), tax));
            taxAmnt = taxAmnt.add(tax);
        }
        this.amount = amnt;
        if (this.netBased) {
            this.netAmount = amnt;
            this.grossAmount = amnt.add(taxAmnt);
        } else {
            this.netAmount = amnt.subtract(taxAmnt);
            this.grossAmount = amnt;
        }
        this.taxQuotas.clear();
        this.taxQuotas.addAll(taxQuts);
        this.taxAmount = taxAmnt;
        this.initialized = true;
    }

    /**
	 * Returns a new {@code TotalPrice} that is the sum of {@code this}
	 * {@code TotalPrice} and the given {@code Price}. The new
	 * {@code TotalPrice} will be net based, if {@code this} {@code TotalPrice}
	 * is net based.
	 * 
	 * @param p
	 *            the {@code Price} to be added to {@code this}
	 *            {@code TotalPrice}.
	 * @return the new {@code TotalPrice}.
	 * @throws IllegalArgumentException
	 *             if the given {@code Price} is {@code null} or its
	 *             {@code Currency} is not equal to the {@code Currency} of this
	 *             {@code TotalPrice}.
	 */
    public final TotalPrice add(final Price p) {
        if (p == null) {
            throw new IllegalArgumentException("The given Price is null.");
        }
        if (!this.currency.equals(p.getCurrency())) {
            throw new IllegalArgumentException("The Currency of the given Price is not equal to the Currency of this TotalPrice.");
        }
        return add(new TotalPrice(p));
    }

    /**
	 * Returns a new {@code TotalPrice} that is the sum of {@code this}
	 * {@code TotalPrice} and the given {@code TotalPrice}. The new
	 * {@code TotalPrice} will be net based, if {@code this} {@code TotalPrice}
	 * is net based.
	 * 
	 * @param tp
	 *            the {@code TotalPrice} to be added to {@code this}
	 *            {@code TotalPrice}.
	 * @return the new {@code TotalPrice}.
	 * @throws IllegalArgumentException
	 *             if the given {@code TotalPrice} is {@code null} or its
	 *             {@code Currency} is not equal to the {@code Currency} of this
	 *             {@code TotalPrice}.
	 */
    public final TotalPrice add(final TotalPrice tp) {
        if (tp == null) {
            throw new IllegalArgumentException("The given TotalPrice is null.");
        }
        if (!this.currency.equals(tp.currency)) {
            throw new IllegalArgumentException("The Currency of the given TotalPrice is not equal to the Currency of this TotalPrice.");
        }
        Map<TaxRate, BigDecimal> map = new HashMap<TaxRate, BigDecimal>();
        for (Quota quota : this.amountQuotas) {
            map.put(quota.getTaxRate(), quota.getAmount());
        }
        BigDecimal amnt;
        for (Quota quota : tp.amountQuotas) {
            if (this.netBased == tp.netBased) {
                amnt = quota.getAmount();
            } else if (this.netBased) {
                amnt = quota.getAmount().subtract(tp.getTaxQuota(quota.getTaxRate()).getAmount());
            } else {
                amnt = quota.getAmount().add(tp.getTaxQuota(quota.getTaxRate()).getAmount());
            }
            if (map.containsKey(quota.getTaxRate())) {
                map.put(quota.getTaxRate(), map.get(quota.getTaxRate()).add(amnt));
            } else {
                map.put(quota.getTaxRate(), amnt);
            }
        }
        return new TotalPrice(map, this.currency, this.netBased);
    }

    /**
	 * Returns a new {@code TotalPrice} that is the difference of {@code this}
	 * {@code TotalPrice} and the given {@code Price}. The new
	 * {@code TotalPrice} will be net based, if {@code this} {@code TotalPrice}
	 * is net based.
	 * 
	 * @param p
	 *            the {@code Price} to be subtracted from {@code this}
	 *            {@code TotalPrice}.
	 * @return the new {@code TotalPrice}.
	 * @throws IllegalArgumentException
	 *             if the given {@code Price} is {@code null} or its
	 *             {@code Currency} is not equal to the {@code Currency} of this
	 *             {@code TotalPrice}.
	 */
    public final TotalPrice subtract(final Price p) {
        if (p == null) {
            throw new IllegalArgumentException("The given Price is null.");
        }
        if (!this.currency.equals(p.getCurrency())) {
            throw new IllegalArgumentException("The Currency of the given Price is not equal to the Currency of this TotalPrice.");
        }
        return subtract(new TotalPrice(p));
    }

    /**
	 * Returns a new {@code TotalPrice} that is the difference of {@code this}
	 * {@code TotalPrice} and the given {@code TotalPrice}. The new
	 * {@code TotalPrice} will be net based, if {@code this} {@code TotalPrice}
	 * is net based.
	 * 
	 * @param tp
	 *            the {@code TotalPrice} to be subtracted from {@code this}
	 *            {@code TotalPrice}.
	 * @return the new {@code TotalPrice}.
	 * @throws IllegalArgumentException
	 *             if the given {@code TotalPrice} is {@code null} or its
	 *             {@code Currency} is not equal to the {@code Currency} of this
	 *             {@code TotalPrice}.
	 */
    public final TotalPrice subtract(final TotalPrice tp) {
        if (tp == null) {
            throw new IllegalArgumentException("The given TotalPrice is null.");
        }
        if (!this.currency.equals(tp.currency)) {
            throw new IllegalArgumentException("The Currency of the given TotalPrice is not equal to the Currency of this TotalPrice.");
        }
        Map<TaxRate, BigDecimal> map = new HashMap<TaxRate, BigDecimal>();
        for (Quota quota : this.amountQuotas) {
            map.put(quota.getTaxRate(), quota.getAmount());
        }
        BigDecimal amnt;
        for (Quota quota : tp.amountQuotas) {
            if (this.netBased == tp.netBased) {
                amnt = quota.getAmount();
            } else if (this.netBased) {
                amnt = quota.getAmount().subtract(tp.getTaxQuota(quota.getTaxRate()).getAmount());
            } else {
                amnt = quota.getAmount().add(tp.getTaxQuota(quota.getTaxRate()).getAmount());
            }
            if (map.containsKey(quota.getTaxRate())) {
                map.put(quota.getTaxRate(), map.get(quota.getTaxRate()).subtract(amnt));
            } else {
                map.put(quota.getTaxRate(), BigDecimal.ZERO.subtract(amnt));
            }
        }
        return new TotalPrice(map, this.currency, this.netBased);
    }

    /**
	 * Compares {@code this} {@code TotalPrice} with the given
	 * {@code TotalPrice} for order.
	 * 
	 * @param tp
	 *            the {@code TotalPrice} to be compared.
	 * @return a negative integer, zero, or a positive integer as {@code this}
	 *         {@code TotalPrice} is less than, equal to, or greater than the
	 *         given {@code TotalPrice}.
	 * @throws IllegalArgumentException
	 *             if the given {@code TotalPrice} is {@code null}.
	 */
    public int compareTo(final TotalPrice tp) {
        if (tp == null) {
            throw new IllegalArgumentException("The given TotalPrice is null.");
        }
        if (this == tp) {
            return 0;
        }
        int result = 0;
        Iterator<Quota> myQuotas = new TreeSet<Quota>(this.amountQuotas).iterator();
        Iterator<Quota> tpQuotas = new TreeSet<Quota>(tp.getAmountQuotas()).iterator();
        while (myQuotas.hasNext() && tpQuotas.hasNext()) {
            result = myQuotas.next().compareTo(tpQuotas.next());
            if (result != 0) {
                return result;
            }
        }
        result = this.amountQuotas.size() - tp.getAmountQuotas().size();
        if (result != 0) {
            return result;
        }
        result = this.currency.getCurrencyCode().compareTo(tp.getCurrency().getCurrencyCode());
        if (result != 0) {
            return result;
        }
        return Boolean.valueOf(this.netBased).compareTo(Boolean.valueOf(tp.isNetBased()));
    }

    /**
	 * Returns the {@code String} representation of this {@code TotalPrice}.
	 * 
	 * @return the {@code String} representation.
	 */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder(getClass().getSimpleName()).append('[');
        for (Quota q : this.amountQuotas) {
            builder.append(q).append(',');
        }
        builder.append(this.currency);
        if (this.netBased) {
            builder.append(",net based]");
        } else {
            builder.append(",gross based]");
        }
        return builder.toString();
    }

    /**
	 * Determines whether the given {@code Object} is equal to {@code this}
	 * {@code TotalPrice}. Two {@code TotalPrice}s are considered equal, if
	 * their amount quotas, {@code Currency}s and net-based-flags are equal.
	 * 
	 * @param o
	 *            the {@code Object}.
	 * @return {@code true}, if the given {@code Object} is equal to
	 *         {@code this} {@code TotalPrice}.
	 */
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TotalPrice)) {
            return false;
        }
        TotalPrice tp = (TotalPrice) o;
        return this.amountQuotas.equals(tp.getAmountQuotas()) && this.currency.equals(tp.getCurrency()) && (this.netBased == tp.isNetBased());
    }

    /**
	 * Returns the hash code for this {@code TotalPrice}.
	 * 
	 * @return the hash code.
	 */
    @Override
    public int hashCode() {
        int result = 0;
        for (Quota q : this.amountQuotas) {
            result = result + q.hashCode();
        }
        return result + this.currency.hashCode() + Boolean.valueOf(this.netBased).hashCode();
    }

    /**
	 * The {@code Quota}.
	 * 
	 * @author Christian Schatt
	 * @version $Revision$
	 */
    @Entity(name = "checkout_Quota")
    @XmlRootElement(name = "quota")
    @XmlAccessorType(XmlAccessType.NONE)
    public static class Quota extends AbstractBasicEntity implements Comparable<Quota> {

        /**
		 * The {@code serialVersionUID}.
		 */
        private static final long serialVersionUID = -1712796240727993158L;

        /**
		 * The {@code TaxRate}.
		 */
        @OneToOne(cascade = CascadeType.ALL, optional = false, orphanRemoval = true)
        @JoinColumn(nullable = false, updatable = false)
        @XmlElement(name = "tax-rate", nillable = false, required = true)
        private TaxRate taxRate;

        /**
		 * The amount.
		 */
        @Column(nullable = false, updatable = false)
        @XmlElement(name = "amount", nillable = false, required = true)
        private BigDecimal amount;

        /**
		 * The no-argument constructor used by JAXB and JPA.
		 */
        @Deprecated
        protected Quota() {
            super();
        }

        /**
		 * The copy constructor.
		 * 
		 * @param q
		 *            the {@code Quota} to be copied.
		 * @throws IllegalArgumentException
		 *             if the given {@code Quota} is {@code null}.
		 */
        public Quota(final Quota q) {
            super();
            if (q == null) {
                throw new IllegalArgumentException("The given Quota is null.");
            }
            this.taxRate = new TaxRate(q.getTaxRate());
            this.amount = q.getAmount();
        }

        /**
		 * Creates a new {@code Quota} from the given {@code TaxRate} and
		 * amount.
		 * 
		 * @param tr
		 *            the {@code TaxRate}.
		 * @param a
		 *            the amount.
		 * @throws IllegalArgumentException
		 *             if the given {@code TaxRate} or amount is {@code null}.
		 */
        public Quota(final TaxRate tr, final BigDecimal a) {
            super();
            if (tr == null) {
                throw new IllegalArgumentException("The given TaxRate is null.");
            }
            if (a == null) {
                throw new IllegalArgumentException("The given amount is null.");
            }
            this.taxRate = new TaxRate(tr);
            this.amount = a;
        }

        /**
		 * Returns the {@code TaxRate}.
		 * 
		 * @return the {@code TaxRate}.
		 */
        public final TaxRate getTaxRate() {
            return this.taxRate;
        }

        /**
		 * Returns the amount.
		 * 
		 * @return the amount.
		 */
        public final BigDecimal getAmount() {
            return this.amount;
        }

        /**
		 * Returns a new {@code Quota} that is the sum of {@code this}
		 * {@code Quota} and the given {@code Quota}.
		 * 
		 * @param q
		 *            the {@code Quota} to be added to {@code this}
		 *            {@code Quota}.
		 * @return the new {@code Quota}.
		 * @throws IllegalArgumentException
		 *             if the given {@code Quota} is {@code null} or its
		 *             {@code TaxRate} is not equal to the {@code TaxRate} of
		 *             this {@code Quota}.
		 */
        public final Quota add(final Quota q) {
            if (q == null) {
                throw new IllegalArgumentException("The given Quota is null.");
            }
            if (!this.taxRate.equals(q.taxRate)) {
                throw new IllegalArgumentException("The TaxRate of the given Quota is not equal to the TaxRate of this Quota.");
            }
            return new Quota(new TaxRate(this.taxRate), this.amount.add(q.amount).setScale(this.amount.scale(), RoundingMode.HALF_UP));
        }

        /**
		 * Returns a new {@code Quota} that is the difference of {@code this}
		 * {@code Quota} and the given {@code Quota}.
		 * 
		 * @param q
		 *            the {@code Quota} to be subtracted from {@code this}
		 *            {@code Quota}.
		 * @return the new {@code Quota}.
		 * @throws IllegalArgumentException
		 *             if the given {@code Quota} is {@code null} or its
		 *             {@code TaxRate} is not equal to the {@code TaxRate} of
		 *             this {@code Quota}.
		 */
        public final Quota subtract(final Quota q) {
            if (q == null) {
                throw new IllegalArgumentException("The given Quota is null.");
            }
            if (!this.taxRate.equals(q.taxRate)) {
                throw new IllegalArgumentException("The TaxRate of the given Quota is not equal to the TaxRate of this Quota.");
            }
            return new Quota(new TaxRate(this.taxRate), this.amount.subtract(q.amount).setScale(this.amount.scale(), RoundingMode.HALF_UP));
        }

        /**
		 * Returns a new {@code Quota} that is the product of {@code this}
		 * {@code Quota} and the given multiplicand.
		 * 
		 * @param m
		 *            the multiplicand.
		 * @return the new {@code Quota}.
		 * @throws IllegalArgumentException
		 *             if the given multiplicand is {@code null}.
		 */
        public final Quota multiply(final BigDecimal m) {
            if (m == null) {
                throw new IllegalArgumentException("The given multiplicand is null.");
            }
            return new Quota(new TaxRate(this.taxRate), this.amount.multiply(m).setScale(this.amount.scale(), RoundingMode.HALF_UP));
        }

        /**
		 * Returns a new {@code Quota} that is the result of the division of
		 * {@code this} {@code Quota} and the given divisor.
		 * 
		 * @param d
		 *            the divisor.
		 * @return the new {@code Quota}.
		 * @throws IllegalArgumentException
		 *             if the given divisor is {@code null}.
		 */
        public final Quota divide(final BigDecimal d) {
            if (d == null) {
                throw new IllegalArgumentException("The given divisor is null.");
            }
            return new Quota(new TaxRate(this.taxRate), this.amount.divide(d, this.amount.scale(), RoundingMode.HALF_UP));
        }

        /**
		 * Compares {@code this} {@code Quota} with the given {@code Quota} for
		 * order.
		 * 
		 * @param q
		 *            the {@code Quota} to be compared.
		 * @return a negative integer, zero, or a positive integer as
		 *         {@code this} {@code Quota} is less than, equal to, or greater
		 *         than the given {@code Quota}.
		 * @throws IllegalArgumentException
		 *             if the given {@code Quota} is {@code null}.
		 */
        public int compareTo(final Quota q) {
            if (q == null) {
                throw new IllegalArgumentException("The given Quota is null.");
            }
            if (this == q) {
                return 0;
            }
            int result = this.taxRate.compareTo(q.getTaxRate());
            if (result != 0) {
                return result;
            }
            return this.amount.compareTo(q.getAmount());
        }

        /**
		 * Returns the {@code String} representation of this {@code Quota}.
		 * 
		 * @return the {@code String} representation.
		 */
        @Override
        public String toString() {
            return new StringBuilder(getClass().getSimpleName()).append('[').append(this.taxRate).append(',').append(this.amount).append(']').toString();
        }

        /**
		 * Determines whether the given {@code Object} is equal to {@code this}
		 * {@code Quota}. Two {@code Quota}s are considered equal, if their
		 * {@code TaxRate}s and amounts are equal.
		 * 
		 * @param o
		 *            the {@code Object}.
		 * @return {@code true}, if the given {@code Object} is equal to
		 *         {@code this} {@code Quota}.
		 */
        @Override
        public boolean equals(final Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof Quota)) {
                return false;
            }
            Quota q = (Quota) o;
            return this.taxRate.equals(q.getTaxRate()) && (this.amount.compareTo(q.getAmount()) == 0);
        }

        /**
		 * Returns the hash code for this {@code Quota}.
		 * 
		 * @return the hash code.
		 */
        @Override
        public int hashCode() {
            return this.taxRate.hashCode() + this.amount.stripTrailingZeros().hashCode();
        }
    }
}
