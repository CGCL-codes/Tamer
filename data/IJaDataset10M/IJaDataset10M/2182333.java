package API.Descriptor;

/**
 * @author SungHun Park (dr.superchamp@gmail.com)
 *
 */
public interface DC_ISO639Language extends DescriptorContent {

    /**
	 * return a total number of languages defined.
	 * @return a total number of languages defined
	 */
    int getLanguagesDefined();

    /**
	 * return a language code.
	 * @param index
	 * @return ISO_639_language_code
	 */
    int getLanguageCodeAt(int index);

    /**
	 * return an audio_type.
	 * @param index
	 * @return audio_type
	 */
    int getAudioTypeAt(int index);

    /**
	 * set add ISO_639_language_code and audio_type.
	 * @param index
	 * @param ISO_639_language_code
	 * @param audio_type
	 * @return
	 */
    boolean setLanguageAt(int index, int ISO_639_language_code, int audio_type);

    /**
	 * add ISO_639_language_code and audio_type.
	 * @param ISO_639_language_code
	 * @param audio_type
	 * @return
	 */
    boolean addLanguage(int ISO_639_language_code, int audio_type);

    /**
	 * add ISO_639_language_code and audio_type.
	 * @param index
	 * @param ISO_639_language_code
	 * @param audio_type
	 * @return
	 */
    boolean addLanguageAt(int index, int ISO_639_language_code, int audio_type);
}
