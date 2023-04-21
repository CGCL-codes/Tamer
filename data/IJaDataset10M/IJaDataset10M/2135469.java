package org.rrd4j.core.timespec;

import org.rrd4j.core.Util;

/**
 * Class which parses at-style time specification (describided in detail on the rrdfetch man page),
 * used in all RRDTool commands. This code is in most parts just a java port of Tobi's parsetime.c
 * code.
 */
public class TimeParser {

    private static final int PREVIOUS_OP = -1;

    TimeToken token;

    TimeScanner scanner;

    TimeSpec spec;

    int op = TimeToken.PLUS;

    int prev_multiplier = -1;

    /**
	 * Constructs TimeParser instance from the given input string.
	 * @param dateString at-style time specification (read rrdfetch man page
	 * for the complete explanation)
	 */
    public TimeParser(String dateString) {
        scanner = new TimeScanner(dateString);
        spec = new TimeSpec(dateString);
    }

    private void expectToken(int desired, String errorMessage) {
        token = scanner.nextToken();
        if (token.id != desired) {
            throw new IllegalArgumentException(errorMessage);
        }
    }

    private void plusMinus(int doop) {
        if (doop >= 0) {
            op = doop;
            expectToken(TimeToken.NUMBER, "There should be number after " + (op == TimeToken.PLUS ? '+' : '-'));
            prev_multiplier = -1;
        }
        int delta = Integer.parseInt(token.value);
        token = scanner.nextToken();
        if (token.id == TimeToken.MONTHS_MINUTES) {
            switch(prev_multiplier) {
                case TimeToken.DAYS:
                case TimeToken.WEEKS:
                case TimeToken.MONTHS:
                case TimeToken.YEARS:
                    token = scanner.resolveMonthsMinutes(TimeToken.MONTHS);
                    break;
                case TimeToken.SECONDS:
                case TimeToken.MINUTES:
                case TimeToken.HOURS:
                    token = scanner.resolveMonthsMinutes(TimeToken.MINUTES);
                    break;
                default:
                    if (delta < 6) {
                        token = scanner.resolveMonthsMinutes(TimeToken.MONTHS);
                    } else {
                        token = scanner.resolveMonthsMinutes(TimeToken.MINUTES);
                    }
            }
        }
        prev_multiplier = token.id;
        delta *= (op == TimeToken.PLUS) ? +1 : -1;
        switch(token.id) {
            case TimeToken.YEARS:
                spec.dyear += delta;
                return;
            case TimeToken.MONTHS:
                spec.dmonth += delta;
                return;
            case TimeToken.WEEKS:
                delta *= 7;
            case TimeToken.DAYS:
                spec.dday += delta;
                return;
            case TimeToken.HOURS:
                spec.dhour += delta;
                return;
            case TimeToken.MINUTES:
                spec.dmin += delta;
                return;
            case TimeToken.SECONDS:
            default:
                spec.dsec += delta;
        }
    }

    private void timeOfDay() {
        int hour, minute = 0;
        scanner.saveState();
        if (token.value.length() > 2) {
            return;
        }
        hour = Integer.parseInt(token.value);
        token = scanner.nextToken();
        if (token.id == TimeToken.SLASH || token.id == TimeToken.DOT) {
            token = scanner.restoreState();
            return;
        }
        if (token.id == TimeToken.COLON) {
            expectToken(TimeToken.NUMBER, "Parsing HH:MM syntax, expecting MM as number, got none");
            minute = Integer.parseInt(token.value);
            if (minute > 59) {
                throw new IllegalArgumentException("Parsing HH:MM syntax, got MM = " + minute + " (>59!)");
            }
            token = scanner.nextToken();
        }
        if (token.id == TimeToken.AM || token.id == TimeToken.PM) {
            if (hour > 12) {
                throw new IllegalArgumentException("There cannot be more than 12 AM or PM hours");
            }
            if (token.id == TimeToken.PM) {
                if (hour != 12) {
                    hour += 12;
                }
            } else {
                if (hour == 12) {
                    hour = 0;
                }
            }
            token = scanner.nextToken();
        } else if (hour > 23) {
            token = scanner.restoreState();
            return;
        }
        spec.hour = hour;
        spec.min = minute;
        spec.sec = 0;
        if (spec.hour == 24) {
            spec.hour = 0;
            spec.day++;
        }
    }

    private void assignDate(long mday, long mon, long year) {
        if (year > 138) {
            if (year > 1970) {
                year -= 1900;
            } else {
                throw new IllegalArgumentException("Invalid year " + year + " (should be either 00-99 or >1900)");
            }
        } else if (year >= 0 && year < 38) {
            year += 100;
        }
        if (year < 70) {
            throw new IllegalArgumentException("Won't handle dates before epoch (01/01/1970), sorry");
        }
        spec.year = (int) year;
        spec.month = (int) mon;
        spec.day = (int) mday;
    }

    private void day() {
        long mday = 0, wday, mon, year = spec.year;
        switch(token.id) {
            case TimeToken.YESTERDAY:
                spec.day--;
            case TimeToken.TODAY:
                token = scanner.nextToken();
                break;
            case TimeToken.TOMORROW:
                spec.day++;
                token = scanner.nextToken();
                break;
            case TimeToken.JAN:
            case TimeToken.FEB:
            case TimeToken.MAR:
            case TimeToken.APR:
            case TimeToken.MAY:
            case TimeToken.JUN:
            case TimeToken.JUL:
            case TimeToken.AUG:
            case TimeToken.SEP:
            case TimeToken.OCT:
            case TimeToken.NOV:
            case TimeToken.DEC:
                mon = (token.id - TimeToken.JAN);
                expectToken(TimeToken.NUMBER, "the day of the month should follow month name");
                mday = Long.parseLong(token.value);
                token = scanner.nextToken();
                if (token.id == TimeToken.NUMBER) {
                    year = Long.parseLong(token.value);
                    token = scanner.nextToken();
                } else {
                    year = spec.year;
                }
                assignDate(mday, mon, year);
                break;
            case TimeToken.SUN:
            case TimeToken.MON:
            case TimeToken.TUE:
            case TimeToken.WED:
            case TimeToken.THU:
            case TimeToken.FRI:
            case TimeToken.SAT:
                wday = (token.id - TimeToken.SUN);
                spec.day += (wday - spec.wday);
                token = scanner.nextToken();
                break;
            case TimeToken.NUMBER:
                mon = Long.parseLong(token.value);
                if (mon > 10L * 365L * 24L * 60L * 60L) {
                    spec.localtime(mon);
                    token = scanner.nextToken();
                    break;
                }
                if (mon > 19700101 && mon < 24000101) {
                    year = mon / 10000;
                    mday = mon % 100;
                    mon = (mon / 100) % 100;
                    token = scanner.nextToken();
                } else {
                    token = scanner.nextToken();
                    if (mon <= 31 && (token.id == TimeToken.SLASH || token.id == TimeToken.DOT)) {
                        int sep = token.id;
                        expectToken(TimeToken.NUMBER, "there should be " + (sep == TimeToken.DOT ? "month" : "day") + " number after " + (sep == TimeToken.DOT ? '.' : '/'));
                        mday = Long.parseLong(token.value);
                        token = scanner.nextToken();
                        if (token.id == sep) {
                            expectToken(TimeToken.NUMBER, "there should be year number after " + (sep == TimeToken.DOT ? '.' : '/'));
                            year = Long.parseLong(token.value);
                            token = scanner.nextToken();
                        }
                        if (sep == TimeToken.DOT) {
                            long x = mday;
                            mday = mon;
                            mon = x;
                        }
                    }
                }
                mon--;
                if (mon < 0 || mon > 11) {
                    throw new IllegalArgumentException("Did you really mean month " + (mon + 1));
                }
                if (mday < 1 || mday > 31) {
                    throw new IllegalArgumentException("I'm afraid that " + mday + " is not a valid day of the month");
                }
                assignDate(mday, mon, year);
                break;
        }
    }

    /**
	 * Parses the input string specified in the constructor.
	 * @return Object representing parsed date/time.
	 */
    public TimeSpec parse() {
        long now = Util.getTime();
        int hr = 0;
        spec.localtime(now);
        token = scanner.nextToken();
        switch(token.id) {
            case TimeToken.PLUS:
            case TimeToken.MINUS:
                break;
            case TimeToken.START:
                spec.type = TimeSpec.TYPE_START;
            case TimeToken.END:
                if (spec.type != TimeSpec.TYPE_START) {
                    spec.type = TimeSpec.TYPE_END;
                }
                spec.year = spec.month = spec.day = spec.hour = spec.min = spec.sec = 0;
            case TimeToken.NOW:
                int time_reference = token.id;
                token = scanner.nextToken();
                if (token.id == TimeToken.PLUS || token.id == TimeToken.MINUS) {
                    break;
                }
                if (time_reference != TimeToken.NOW) {
                    throw new IllegalArgumentException("Words 'start' or 'end' MUST be followed by +|- offset");
                } else if (token.id != TimeToken.EOF) {
                    throw new IllegalArgumentException("If 'now' is followed by a token it must be +|- offset");
                }
                break;
            case TimeToken.NUMBER:
                timeOfDay();
                if (token.id != TimeToken.NUMBER) {
                    break;
                }
            case TimeToken.JAN:
            case TimeToken.FEB:
            case TimeToken.MAR:
            case TimeToken.APR:
            case TimeToken.MAY:
            case TimeToken.JUN:
            case TimeToken.JUL:
            case TimeToken.AUG:
            case TimeToken.SEP:
            case TimeToken.OCT:
            case TimeToken.NOV:
            case TimeToken.DEC:
                day();
                if (token.id != TimeToken.NUMBER) {
                    break;
                }
                timeOfDay();
                break;
            case TimeToken.TEATIME:
                hr += 4;
            case TimeToken.NOON:
                hr += 12;
            case TimeToken.MIDNIGHT:
                spec.hour = hr;
                spec.min = 0;
                spec.sec = 0;
                token = scanner.nextToken();
                day();
                break;
            default:
                throw new IllegalArgumentException("Unparsable time: " + token.value);
        }
        if (token.id == TimeToken.PLUS || token.id == TimeToken.MINUS) {
            scanner.setContext(false);
            while (token.id == TimeToken.PLUS || token.id == TimeToken.MINUS || token.id == TimeToken.NUMBER) {
                if (token.id == TimeToken.NUMBER) {
                    plusMinus(PREVIOUS_OP);
                } else {
                    plusMinus(token.id);
                }
                token = scanner.nextToken();
            }
        }
        if (token.id != TimeToken.EOF) {
            throw new IllegalArgumentException("Unparsable trailing text: " + token.value);
        }
        return spec;
    }
}
