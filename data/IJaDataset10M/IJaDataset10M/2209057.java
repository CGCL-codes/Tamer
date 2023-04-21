/**
 * Redistribution and use of this software and associated documentation
 * ("Software"), with or without modification, are permitted provided
 * that the following conditions are met:
 *
 * 1. Redistributions of source code must retain copyright
 *    statements and notices.  Redistributions must also contain a
 *    copy of this document.
 *
 * 2. Redistributions in binary form must reproduce the
 *    above copyright notice, this list of conditions and the
 *    following disclaimer in the documentation and/or other
 *    materials provided with the distribution.
 *
 * 3. The name "Exolab" must not be used to endorse or promote
 *    products derived from this Software without prior written
 *    permission of Intalio.  For written permission,
 *    please contact info@exolab.org.
 *
 * 4. Products derived from this Software may not be called "Exolab"
 *    nor may "Exolab" appear in their names without prior written
 *    permission of Intalio. Exolab is a registered
 *    trademark of Intalio.
 *
 * 5. Due credit should be given to the Exolab Project
 *    (http://www.exolab.org/).
 *
 * THIS SOFTWARE IS PROVIDED BY INTALIO AND CONTRIBUTORS
 * ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT
 * NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL
 * INTALIO OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * Copyright 2000 (C) Intalio Inc. All Rights Reserved.
 *
 */


package tyrex.services;

import java.util.Arrays;
import java.util.Comparator;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.FileInputStream;
import java.io.File;

import junit.framework.*;
import junit.extensions.*;

/**
 *
 * @author <a href="mailto:mills@intalio.com">David Mills</a>
 * @version $Revision: 1.8 $
 */


public class UUIDTest extends TestCase
{
    private PrintWriter _logger = null;

    public UUIDTest(String name)
    {
        super(name);
    }

    public void setUp()
    {
        _logger= new PrintWriter(System.out);
    }

    public void tearDown()
    {
        _logger.flush();
    }


    /**
     * <p>All the methods of the class is static and so an instance of
     * the class is not required for the tests.  Apart from the
     * methods fromBytes() and toBytes() it is not possible to fully
     * verify that the ids returned truely conform to the IETF draft.
     * Testing can only verify that the ids are of the correct
     * form.</p>
     *
     * <p>Set up several prefixes both short and long.</p>
     *
     * @result Call create() without a prefix argument.  It should
     * return a String 36 characters in length.  Call it again with an
     * empty prefix.  Again it should return a 36 character String.
     * Call it with a single character prefix.  It should return a 37
     * character String.  In general it should return a string whose
     * length is 36 plus the length of the prefix.  Calling trim() and
     * any id that is not longer than MAXIMUM_LENGTH should result in
     * the same String.  Longer ids will be truncated to the
     * MAXIMUM_LENGTH.
     *
     * <p>Using an example id call toBytes() and then fromBytes() on
     * the result.  The original id should be returned.</p>
     */

    public void testBasicFunctionality()
        throws Exception
    {
        String emptyPrefix = "";
        String smallestPrefix = "p";
        String smallPrefix = "prefix";
        String prefix = "ALongerPrefix";
        String longerThanMaximumPrefix = "AMaximumPrefixXXXXXXXXXXXXXXXXXXXXX";

        // This loop is most certainly not needed - but just in case.
        while (longerThanMaximumPrefix.length() < UUID.MAXIMUM_PREFIX)
        {
            longerThanMaximumPrefix = longerThanMaximumPrefix
                + longerThanMaximumPrefix;
        }
        String maximumPrefix
            = longerThanMaximumPrefix.substring(0, UUID.MAXIMUM_PREFIX);
        String id =  UUID.create();
        assertEquals("ID length", 36, id.length());
        assertEquals("Trimmed ID", id, UUID.trim(id));
        id =  UUID.create(emptyPrefix);
        assertEquals("ID length", 36, id.length());
        assertEquals("Trimmed ID", id, UUID.trim(id));
        id =  UUID.create(smallestPrefix);
        assertEquals("ID length", 37, id.length());
        assertEquals("Trimmed ID", id, UUID.trim(id));
        id =  UUID.create(prefix);
        assertEquals("ID length", 36 + prefix.length(), id.length());
        assertEquals("Trimmed ID", id, UUID.trim(id));
        id =  UUID.create(maximumPrefix);
        assertEquals("ID length", UUID.MAXIMUM_LENGTH, id.length());
        assertEquals("Trimmed ID", id, UUID.trim(id));
        id =  UUID.create(longerThanMaximumPrefix);
        assert("ID length", id.length() > UUID.MAXIMUM_LENGTH);
        assertEquals("Trimmed ID length", UUID.MAXIMUM_LENGTH,
                     UUID.trim(UUID.create(longerThanMaximumPrefix)).length());
        assertEquals("Trimmed ID", id.substring(0, UUID.MAXIMUM_LENGTH),
                     UUID.trim(id));
        String anID = "a8f1da0a48767f8404103eb2ff74084f";
        byte[] bytes = UUID.toBytes(anID);
        assertEquals("Converted ID", anID, UUID.fromBytes(bytes));
        anID = "prefixa8f1da0a48767f8404103eb2ff74084f";
        bytes = UUID.toBytes("prefix", anID);
        assertEquals("Coverted ID", anID, UUID.fromBytes("prefix", bytes));

        char[] chars = UUID.createTimeUUIDChars();
//        System.out.println(chars);
        bytes = UUID.createTimeUUIDBytes();
//        System.out.println(bytes);
        bytes = UUID.createBinary();
//        System.out.println(bytes);
    }


    /**
     * <p>Generate a large number of IDs and ensure that all are
     * unique.</p>
     *
     * @result Create a large String array.  Fill the array by
     * generating a large number of individual ids.  Sort the array
     * and then traverse it ensuring that no entry is the same as its
     * neighbors.
     */

    public void uniquenessOfCreateTimeUUIDBytes(int variant)
        throws Exception
    {
        byte[] bytes;
        final int numIds = 700000;
        byte[][] ids1 = new byte[2 * numIds][8];
        byte[][] ids2 = new byte[numIds][8];
        IdThread idThread = new IdThread(ids2, variant);
        idThread.start();
        for (int i = 0; i < numIds; i++)
        {
            if (variant == 1)
            {
                bytes = UUID.createTimeUUIDBytes();
            }
            else
            {
                bytes = UUID.createBinary();
            }
            for (int j = 0; j < 8; j++)
            {
                if (j < 6)
                {
                    ids1[i][j] = bytes[j];
                }
                else
                {
                    ids1[i][j] = bytes[j + 2];
                }
            }
        }
        idThread.join();
        for (int i = 0; i < numIds; i++)
        {
            ids1[numIds + i] = ids2[i];
        }
        BytesCompare comp = new BytesCompare();
        Arrays.sort(ids1, comp);
        for (int i = 1; i < numIds; i++)
        {
            assert("Unique", comp.compare(ids1[i], ids1[i - 1]) != 0);
        }
    }


    /**
     * <p>Generate a large number of IDs and ensure that all are
     * unique.</p>
     *
     * @result Create a large String array.  Fill the array by
     * generating a large number of individual ids.  Sort the array
     * and then traverse it ensuring that no entry is the same as its
     * neighbors.
     */

    public void testUniquenessOfCreateTimeUUIDBytes()
        throws Exception
    {
        uniquenessOfCreateTimeUUIDBytes(1);
        uniquenessOfCreateTimeUUIDBytes(2);
    }


    /** Adds a message in the log (except if the log is null)*/
    private void logMessage(String message)
    {
        if (_logger != null)
        {
            _logger.println(message);
        }
    }


    // Allow this test to be run on its own.
    public static void main(String args[])
    {
        tyrex.Unit.runTests(args, new TestSuite(UUIDTest.class));
    }


    private class BytesCompare implements Comparator
    {
        public int compare(Object oId1, Object oId2)
        {
            byte[] id1 = (byte[])oId1;
            byte[] id2 = (byte[])oId2;
            for (int i = 0; i < id1.length; i++)
            {
                if (id1[i] < id2[i])
                {
                    return -1;
                }
                else if (id1[i] > id2[i])
                {
                    return 1;
                }
            }
            return 0;
        }

        public boolean equals()
        {
            return false;
        }
    }

    private class IdThread extends Thread
    {
        private byte[][] _ids = null;
        private int _variant;

        public IdThread(byte[][] ids, int variant)
        {
            _ids = ids;
            _variant = variant;
        }

        public void run()
        {
            byte[] bytes;
            for (int i = 0; i < _ids.length; i++)
            {
                if (_variant == 1)
                {
                    bytes = UUID.createTimeUUIDBytes();
                }
                else
                {
                    bytes = UUID.createBinary();
                }
                for (int j = 0; j < 8; j++)
                {
                    if (j < 6)
                    {
                        _ids[i][j] = bytes[j];
                    }
                    else
                    {
                        _ids[i][j] = bytes[j + 2];
                    }
                }
            }
        }
    }
}
