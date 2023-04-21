/*
 * Country.java
 *
 * Created on Jan 1, 2008, 9:25:43 PM
 *
 * Copyright (c) Joerg Wassmer
 * This library is free software. You can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License version 2 or above
 * as published by the Free Software Foundation.
 * For more information please visit <http://jaxlib.sourceforge.net>.
 */

package jaxlib.i18n;

import java.io.IOError;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Serializable;
import java.lang.ref.SoftReference;
import java.net.URL;
import java.util.List;
import java.util.Locale;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import jaxlib.col.ArrayXList;
import jaxlib.col.HashXMap;
import jaxlib.col.SortedObjectArray;
import jaxlib.conversion.ConversionMethod;
import jaxlib.conversion.ConvertableType;
import jaxlib.i18n.xml.CountryXmlAdapter;
import jaxlib.io.IO;
import jaxlib.lang.Ints;



/**
 * A <a href="http://en.wikipedia.org/wiki/ISO_3166">ISO 3166</a> country code.
 * <p>
 * This class supports the 2- as well as the 3-letter codes.
 * </p>
 *
 * @author  jw
 * @since   JaXLib 1.0
 * @version $Id: Country.java 3059 2012-03-01 08:58:41Z joerg_wassmer $
 */
@ConvertableType
@XmlRootElement     (name="country", namespace="urn:jaxlib.i18n")
@XmlType            (name="Country", namespace="urn:jaxlib.i18n")
@XmlJavaTypeAdapter (CountryXmlAdapter.class)
@ParametersAreNonnullByDefault
public final class Country extends Object implements Comparable<Country>, Serializable
{

  /**
   * @since JaXLib 1.0
   */
  private static final long serialVersionUID = 1L;


  //private static final int CHAR_MASK_0  = 0x00007c00;
  //private static final int CHAR_MASK_1  = 0x000003e0;
  //private static final int CHAR_MASK_2  = 0x0000001f;
  private static final int ISO_MASK     = 0x00007fff;
  private static final int CURRENT_MASK = 0x00008000;




  /**
   * @serial
   * @since JaXLib 1.0
   */
  private final int bits;

  private final transient String isoA2;
  private final transient String isoA3;
  private transient int ordinal;



  private Country(final int bits, final String isoA3, final String isoA2)
  {
    super();
    this.bits   = bits;
    this.isoA2  = isoA2;
    this.isoA3  = isoA3;
  }



  /**
   * @serialData
   * @since JaXLib 1.0
   */
  private Object readResolve()
  {
    return id(getId());
  }



  @Override
  public final int compareTo(@Nullable final Country b)
  {
    return (b == null) ? 1 : Ints.cmp(this.bits & ISO_MASK, b.bits & ISO_MASK);
  }



  /**
   * Get the most compatible {@code ISO-3166} code of this country.
   * For now this method always returns the {@link #getIsoA2() ISO-3166-1-A2} code. This method mainly exists for
   * eventual future needs, and to be consistent with the {@link Language#getCode() Language} class.
   *
   * @return
   *  {@link #getIsoA2()}.
   *
   * @since JaXLib 1.0
   */
  @Nonnull
  public final String getCode()
  {
    return this.isoA2;
  }



  /**
   * Get an URL to a SVG image of the flag of this country.
   * The images were taken from <a href="http://www.wikipedia.org">Wikipedia</a>.
   *
   * @return
   *  the URL to the image resource; {@code null} if unavailable.
   *
   * @since JaXLib 1.0
   */
  @CheckForNull
  public final URL getFlagImageResource()
  {
    return Country.class.getResource("resources/flags/" + this.isoA3 + ".svg");
  }



  /**
   * A non-offical constant 2-byte positive numeric representation of this country.
   *
   * @return
   *  a number between {@code 1} and {@code 32767}.
   *
   * @since JaXLib 1.0
   */
  public final int getId()
  {
    return (this.bits & ISO_MASK) + 1;
  }



  /**
   * The {@code ISO-3166-1-A2} country code, an uppercase 2-letter string containing letters {@code [A-Z]} exclusively.
   * These codes are used by {@link java.util.Locale}.
   *
   * @return
   *  the code, for now never {@code null}. In case of {@code ISO} standard changes this method may return {@code null}
   *  in future versions of this class. However, this seems to be unlikely.
   *
   * @since JaXLib 1.0
   */
  public final String getIsoA2()
  {
    return this.isoA2;
  }



  /**
   * The {@code ISO-3166-1-A3} country code, an uppercase 3-letter string containing letters {@code [A-Z]} exclusively.
   *
   * @return
   *  the code, for now never {@code null}. In case of {@code ISO} standard changes this method may return {@code null}
   *  in future versions of this class. However, this seems to be unlikely.
   *
   * @since JaXLib 1.0
   */
  public final String getIsoA3()
  {
    return this.isoA3;
  }



  /**
   * Get the display name of this country for the specified locale.
   *
   * @return
   *  the display name; the country code if not available.
   *
   * @see Locale#getDisplayCountry(Locale)
   */
  @Nonnull
  public final String getName(@Nullable final Locale locale)
  {
    return new Locale(null, (this.isoA2 == null) ? this.isoA3 : this.isoA2)
      .getDisplayCountry((locale == null) ? Locale.getDefault() : locale);
  }



  /**
   * Get the official english name assigned to this country by the {@code ISO} standard maintainers.
   * The official name of a country may change from time to time together with its form of government or its
   * territory.
   *
   * @since JaXLib 1.0
   */
  @Nonnull
  public final String getReferenceName()
  {
    final String name = Country.Cache.getReferenceNames()[this.ordinal];
    return (name != null) ? name : this.isoA3;
  }



  /**
   * Whether this country code was current at time of publishing of this version of this API.
   * A country code is current if the country is existing and the country code has not been withdrawn for any reason.
   *
   * @since JaXLib 1.0
   */
  public final boolean isCurrent()
  {
    return (this.bits & CURRENT_MASK) != 0;
  }



  @Override
  @Nonnull
  public final String toString()
  {
    return this.isoA2;
  }




  @CheckForNull
  public static Country get(@Nullable final String code)
  {
    return Country.Cache.map.get(code);
  }



  /**
   * Get the country for the specified id, which has to be a number as returned by {@link #getId()}.
   *
   * @throws IllegalArgumentException
   *  if there is no country associated with the specified id.
   *
   * @since JaXLib 1.0
   */
  @Nonnull
  public static Country id(final int v)
  {
    final Country c = Country.Cache.map.get(Country.class, v);
    if (c == null)
      throw new IllegalArgumentException(Integer.toString(v));
    return c;
  }



  @ConversionMethod
  @Nonnull
  public static Country iso(final String code)
  {
    final Country c = Country.Cache.map.get(code);
    if (c == null)
    {
      checkCode(code);
      throw new IllegalArgumentException("unknown country code: " + code);
    }
    return c;
  }


  
  /**
   * Get a list of all countries known to this class.
   *
   * @return
   *  the list of countries; neither {@code null} nor empty.
   *
   * @since JaXLib 1.0
   */
  @Nonnull
  public static List<Country> list()
  {
    return Country.Cache.instanceList;
  }



  private static void checkCode(final String code)
  {
    final int len = code.length();
    if ((len != 2) && (len != 3))
      throw new IllegalArgumentException("not a ISO-3166 code, illegal length: " + code);

    for (int i = len; --i >= 0;)
    {
      final char c = code.charAt(i);
      if ((c < 'A') || (c > 'Z'))
        throw new IllegalArgumentException("not a ISO-3166 code, illegal char: " + code);
    }
  }


  /*
  private static String decodeA3(final int bits)
  {
    return new String(new char[] {
      (char) (((bits & CHAR_MASK_0) >>> 10) + 'A'),
      (char) (((bits & CHAR_MASK_1) >>>  5) + 'A'),
      (char) ( (bits & CHAR_MASK_2)         + 'A')
    });
  }*/





  private static final class Cache extends Object
  {

    @Nonnull
    static final Country[] instances;

    @Nonnull
    static final SortedObjectArray<Country> instanceList;

    @Nonnull
    static final HashXMap<Object,Country> map;

    @CheckForNull
    static SoftReference<String[]> referenceNames;


    static
    {
      final ArrayXList<Country> list = new ArrayXList<>(Country.class, 268);
      final InputStream         bin  = Country.class.getResourceAsStream("Country.utf8");
      if (bin == null)
        throw new Error("missing resource Country.utf8");

      try
      {
        final Reader in  = new InputStreamReader(bin, "UTF-8");
        final char[] buf = new char[12];
        READ: while (true)
        {
          for (int off = 0; off < 12;)
          {
            final int step = in.read(buf, off, 12 - off);
            if (step < 0)
            {
              if (off != 0)
                throw new Error("corrupted Country.utf8");
              break READ;
            }
            off += step;
          }

          int bits = encodeA3(buf[0], buf[1], buf[2]);
          final char state = buf[11];
          if (state == '+')
            bits |= CURRENT_MASK;
          else if (state != '-')
            throw new Error("corrupted Country.utf8");

          list.add(new Country(bits, new String(buf, 0, 3).intern(), new String(buf, 4, 2).intern()));

          while (true)
          {
            final int b = in.read();
            if (b < 0)
              break READ;
            if (b == '\n')
              continue READ;
          }
        }

        if (list.isEmpty())
          throw new Error("corrupted Country.utf8");
      }
      catch (final IOException ex)
      {
        throw new IOError(ex);
      }
      finally
      {
        IO.tryClose(bin);
      }

      list.trimToSize();
      instances    = (Country[]) list.dispose();
      instanceList = SortedObjectArray.readOnly(instances);
      map          = new HashXMap<>(instances.length * 2);

      for (int i = 0; i < instances.length; i++)
      {
        final Country c = instances[i];
        c.ordinal = i;
        if ((map.put(c.isoA2, c) != null) || (map.put(c.isoA3, c) != null))
          throw new AssertionError(c.isoA3);
        map.put(Country.class, c.getId(), c);
      }
    }



    static String[] getReferenceNames()
    {
      SoftReference<String[]> ref   = Country.Cache.referenceNames;
      String[]                names = (ref == null) ? null : ref.get();
      if (names == null)
      {
        final InputStream bin  = Country.class.getResourceAsStream("Country.utf8");
        if (bin == null)
          throw new Error("missing resource Country.utf8");

        try
        {
          int i = 0;
          names = new String[Country.Cache.instances.length];
          final InputStreamReader in = new InputStreamReader(bin, "UTF-8");
          final StringBuilder     sb = new StringBuilder(64);

          READ: while (true)
          {
            for (long off = 0; off < 13;)
            {
              final long step = in.skip(13 - off);
              if (step <= 0)
              {
                if (off != 0)
                  throw new Error("corrupted Country.utf8");
                break READ;
              }
              off += step;
            }

            sb.setLength(0);
            while (true)
            {
              final int c = in.read();
              if (c == '\n')
              {
                names[i++] = sb.toString();
                break;
              }
              else if (c >= 0)
                sb.append((char) c);
              else
                break;
            }
          }
        }
        catch (final IOException ex)
        {
          throw new IOError(ex);
        }
        finally
        {
          IO.tryClose(bin);
        }

        Country.Cache.referenceNames = new SoftReference<>(names);
      }
      return names;
    }



    private static int encodeA3(final int c0, final int c1, final int c2)
    {
      return ((c0 - 'A') << 10)
          | ((c1 - 'A') <<  5)
          |  (c2 - 'A');
    }



    private Cache()
    {
      super();
    }

  }

}
