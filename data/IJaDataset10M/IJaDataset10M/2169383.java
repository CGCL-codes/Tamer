package org.demis.elf.locale;

import java.util.Collection;

/**
  * DAO (Data Access Object) interface for Locale.
  */
public interface LocaleDAO {

    public Locale findById(java.lang.String localeId);

    public Collection<Locale> findByExemple(Locale locale);

    public int findCount(final Locale locale);

    public void save(Locale locale);

    public void saveAll(final Collection<Locale> locales);

    public void delete(Locale locale);

    public void deleteAll(final Collection<Locale> locales);
}
