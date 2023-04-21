// Copyright 2007 Konrad Twardowski
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//   http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package org.makagiga.commons.io;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;
import java.util.Locale;

import org.makagiga.commons.FS;
import org.makagiga.commons.annotation.Uninstantiable;

/**
 * @since 2.0, 4.0 (org.makagiga.commons.io package)
 */
public final class Checksum {
	
	// public
	
	public static byte[] get(final String algorithm, final InputStream input) throws IOException, NoSuchAlgorithmException {
		MessageDigest digest = MessageDigest.getInstance(algorithm);
		DigestInputStream dis = new DigestInputStream(input, digest);
		FS.copyStream(dis, null);
		
		return digest.digest();
	}
	
	public static byte[] get(final String algorithm, final File file) throws IOException, NoSuchAlgorithmException {
		try (FS.BufferedFileInput input = new FS.BufferedFileInput(file)) {
			return get(algorithm, input);
		}
	}

	/**
	 * CREDITS: http://blogs.sun.com/roller/page/andreas?entry=hashing_a_file_in_3
	 */
	public static String toString(final byte[] digest) {
		Formatter f = new Formatter(Locale.US);
		
		return f.format("%0" + (digest.length * 2) + "x", new BigInteger(1, digest)).toString();
	}

	// private
	
	@Uninstantiable
	private Checksum() { }

}
